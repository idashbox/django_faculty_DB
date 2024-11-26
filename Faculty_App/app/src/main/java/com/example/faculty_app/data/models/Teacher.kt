package com.example.faculty_app.data.models

data class Teacher(
    val id: Int,
    val user: User,
    val department: Int,
    val year_of_start_of_work: String
)
