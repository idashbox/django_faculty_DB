package com.example.faculty_app.data.repositories

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
        department: Int? = null,
        orderBy: String? = null
    ): Response<TeacherResponse> {
        return apiService.getTeachers(page, pageSize, name, department, orderBy)
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
}
