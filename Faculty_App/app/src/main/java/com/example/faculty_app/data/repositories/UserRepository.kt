package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.models.UserResponse
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class UserRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getUsers(
        page: Int,
        pageSize: Int,
        name: String? = null,
        orderBy: String? = null
    ): Response<UserResponse> {
        return apiService.getUsers(page, pageSize, name, orderBy)
    }

    suspend fun getUser(id: Int): Response<User> {
        return apiService.getUser(id)
    }

    suspend fun createUser(user: User): Response<User> {
        return apiService.createUser(user)
    }

    suspend fun updateUser(id: Int, user: User): Response<User> {
        return apiService.updateUser(id, user)
    }

    suspend fun deleteUser(id: Int): Response<Void> {
        return apiService.deleteUser(id)
    }
}
