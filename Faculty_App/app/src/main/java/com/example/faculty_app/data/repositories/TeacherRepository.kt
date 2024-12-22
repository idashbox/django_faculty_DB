package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.DepartmentResponse
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.TeacherResponse
import com.example.faculty_app.data.models.TeacherStatistics
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class TeacherRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getTeachers(
        page: Int,
        pageSize: Int,
        name: String? = null,
        surname: String? = null,
        middleName: String? = null,
        birthday: String? = null,
        department: Int? = null,
        orderBy: String? = null
    ): Response<TeacherResponse> {
        return apiService.getTeachers(page, pageSize, name, surname, middleName, birthday, department, orderBy)
    }


    suspend fun getTeacher(id: Int): Response<Teacher> {
        return apiService.getTeacher(id)
    }

    suspend fun createTeacher(teacher: Teacher): Response<Teacher> {
        return apiService.createTeacher(teacher)
    }

    suspend fun updateTeacher(id: Int, teacher: Teacher): Response<Teacher> {
        return apiService.updateTeacher(id, teacher)
    }

    suspend fun deleteTeacher(id: Int): Response<Void> {
        return apiService.deleteTeacher(id)
    }

    suspend fun getStatistics(): Response<TeacherStatistics> {
        return apiService.getStatistics()
    }
    suspend fun getDepartments(
        page: Int,
        pageSize: Int,
        name: String? = null,
        orderBy: String? = null
    ): Response<DepartmentResponse> {
        return apiService.getDepartments(page, pageSize, name, orderBy)
    }
    suspend fun searchTeachers(
        page: Int,
        pageSize: Int,
        query: String
    ): Response<TeacherResponse> {
        return apiService.searchTeachers(page, pageSize, query)
    }
}
