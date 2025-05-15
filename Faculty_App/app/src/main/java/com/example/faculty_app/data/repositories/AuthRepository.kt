package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.AuthResponse
import com.example.faculty_app.data.models.AuthToken
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.AuthRequest
import com.example.faculty_app.data.network.AuthService
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class AuthRepository() {
    suspend fun login(username: String, password: String): Response<AuthToken> {
        return RetrofitClient.authService.login(AuthRequest(username, password))
    }
}