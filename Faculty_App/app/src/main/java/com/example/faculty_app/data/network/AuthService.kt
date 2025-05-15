package com.example.faculty_app.data.network

import com.example.faculty_app.data.models.AuthToken
import com.example.faculty_app.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("login/")
    suspend fun login(@Body request: AuthRequest): Response<AuthToken>
}
