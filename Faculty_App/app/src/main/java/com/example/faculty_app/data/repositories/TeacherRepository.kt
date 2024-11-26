package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.Teacher
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TeacherRepository {

    private val apiService: ApiService
    init {

        apiService = retrofit.create(ApiService::class.java)
    }

    // Получение всех преподавателей
    fun getTeachers(): Call<List<Teacher>> {
        return apiService.getTeachers()
    }

    // Получение одного преподавателя по id
    fun getTeacher(id: Int): Call<Teacher> {
        return apiService.getTeacher(id)
    }

    // Создание нового преподавателя
    fun createTeacher(teacher: Teacher): Call<Teacher> {
        return apiService.createTeacher(teacher)
    }

    // Обновление информации о преподавателе
    fun updateTeacher(id: Int, teacher: Teacher): Call<Teacher> {
        return apiService.updateTeacher(id, teacher)
    }

    // Удаление преподавателя
    fun deleteTeacher(id: Int): Call<Void> {
        return apiService.deleteTeacher(id)
    }
}
