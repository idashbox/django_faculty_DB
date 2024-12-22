package com.example.faculty_app.data.models

data class UserResponse(
    val results: List<User>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
