package com.example.faculty_app.data.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val login: String,
    val password: String,
    val surname: String,
    val middle_name: String?,
    val birthday: String,
    val sex: String?,
    val role: String?
)
