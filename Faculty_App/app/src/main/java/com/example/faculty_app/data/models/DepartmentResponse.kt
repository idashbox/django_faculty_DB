package com.example.faculty_app.data.models

data class DepartmentResponse(
    val results: List<Department>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
