package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.DepartmentResponse
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.models.DirectionResponse
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class DirectionRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getDirections(
        page: Int,
        pageSize: Int,
        title: String?,
        orderBy: String? = null
    ): Response<DirectionResponse> {
        return apiService.getDirections(page, pageSize, title, orderBy)
    }

    suspend fun getDirection(id: Int): Response<Direction> {
        return apiService.getDirection(id)
    }

    suspend fun createDirection(direction: Direction):Response<Direction> {
        return apiService.createDirection(direction)
    }

    suspend fun updateDirection(id: Int, direction: Direction): Response<Direction> {
        return apiService.updateDirection(id, direction)
    }

    suspend fun deleteDirection(id: Int): Response<Void> {
        return apiService.deleteDirection(id)
    }

    suspend fun getDepartments(
        page: Int,
        pageSize: Int,
        name: String? = null,
        orderBy: String? = null
    ): Response<DepartmentResponse> {
        return apiService.getDepartments(page, pageSize, name, orderBy)
    }
}
