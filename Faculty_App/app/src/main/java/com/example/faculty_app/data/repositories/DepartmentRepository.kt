package com.example.faculty_app.data.repositories

import android.icu.text.CaseMap.Title
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.DepartmentResponse
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class DepartmentRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getDepartments(
        page: Int,
        pageSize: Int,
        title: String?,
        orderBy: String? = null
    ): Response<DepartmentResponse> {
        return apiService.getDepartments(page, pageSize, title, orderBy)
    }

    suspend fun getDepartment(id: Int): Response<Department> {
        return apiService.getDepartment(id)
    }

    suspend fun createDepartment(department: Department): Response<Department> {
        return apiService.createDepartment(department)
    }

    suspend fun updateDepartment(id: Int, department: Department): Response<Department> {
        return apiService.updateDepartment(id, department)
    }

    suspend fun deleteDepartment(id: Int): Response<Void> {
        return apiService.deleteDepartment(id)
    }
}
