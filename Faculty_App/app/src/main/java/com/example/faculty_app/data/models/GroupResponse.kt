package com.example.faculty_app.data.models

data class GroupResponse(
    val results: List<Group>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)