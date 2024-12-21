//package com.example.faculty_app.data.pagging
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.faculty_app.data.models.Teacher
//import com.example.faculty_app.data.repositories.TeacherRepository
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class TeacherPagingSource(
//    private val teacherRepository: TeacherRepository,
//    private val query: String,
//    private val filter: String
//) : PagingSource<Int, Teacher>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Teacher> {
//        val page = params.key ?: 1
//        return try {
//            teacherRepository.getTeachersPaged(page, params.loadSize).enqueue(object : Callback<List<Teacher>> {
//                override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
//                    if (response.isSuccessful) {
//                        val teachers = response.body() ?: emptyList()
//
//                        // Фильтрация и поиск
//                        val filteredTeachers = teachers.filter { teacher ->
//                            teacher.name.contains(query, ignoreCase = true)
//                        }.sortedBy { teacher ->
//                            teacher.name
//                        }
//
//                        LoadResult.Page(
//                            data = filteredTeachers,
//                            prevKey = if (page == 1) null else page - 1,
//                            nextKey = if (filteredTeachers.isEmpty()) null else page + 1
//                        )
//                    } else {
//                        LoadResult.Error(Exception("Ошибка ответа сервера"))
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
//                    LoadResult.Error(t)
//                }
//            })
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Teacher>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}
