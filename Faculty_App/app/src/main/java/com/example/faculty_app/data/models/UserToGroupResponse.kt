package com.example.faculty_app.data.models

data class UserToGroupResponse(
    val results: List<UserToGroup>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)