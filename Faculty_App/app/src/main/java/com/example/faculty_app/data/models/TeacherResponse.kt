package com.example.faculty_app.data.models

data class TeacherResponse(
    val results: List<Teacher>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
