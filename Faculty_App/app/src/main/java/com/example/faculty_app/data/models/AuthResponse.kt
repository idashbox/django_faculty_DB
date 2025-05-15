package com.example.faculty_app.data.models

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)