package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {

    private val apiService: ApiService

    init {

        apiService = retrofit.create(ApiService::class.java)
    }

    // Получение всех пользователей
    fun getUsers(): Call<List<User>> {
        return apiService.getUsers()
    }

    // Получение одного пользователя по id
    fun getUser(id: Int): Call<User> {
        return apiService.getUser(id)
    }

    // Создание нового пользователя
    fun createUser(user: User): Call<User> {
        return apiService.createUser(user)
    }

    // Обновление информации о пользователе
    fun updateUser(id: Int, user: User): Call<User> {
        return apiService.updateUser(id, user)
    }

    // Удаление пользователя
    fun deleteUser(id: Int): Call<Void> {
        return apiService.deleteUser(id)
    }
}
