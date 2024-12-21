package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.Direction
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DirectionRepository {

    private val apiService: ApiService
    init {
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getDirections(): Call<List<Direction>> {
        return apiService.getDirections()
    }

    fun getDirection(id: Int): Call<Direction> {
        return apiService.getDirection(id)
    }

    fun createDirection(direction: Direction): Call<Direction> {
        return apiService.createDirection(direction)
    }

    fun updateDirection(id: Int, direction: Direction): Call<Direction> {
        return apiService.updateDirection(id, direction)
    }

    fun deleteDirection(id: Int): Call<Void> {
        return apiService.deleteDirection(id)
    }
}
