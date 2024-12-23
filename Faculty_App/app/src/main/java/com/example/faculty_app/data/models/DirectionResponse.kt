package com.example.faculty_app.data.models

data class DirectionResponse(
    val results: List<Direction>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
