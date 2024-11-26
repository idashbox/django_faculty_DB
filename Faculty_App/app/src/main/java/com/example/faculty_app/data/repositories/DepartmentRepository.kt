package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.Department
import retrofit2.Call

class DepartmentRepository {

    private val apiService: ApiService

    init {

        apiService = retrofit.create(ApiService::class.java)
    }

    // Получение всех кафедр
    fun getDepartments(): Call<List<Department>> {
        return apiService.getDepartments()
    }

    // Получение одной кафедры по id
    fun getDepartment(id: Int): Call<Department> {
        return apiService.getDepartment(id)
    }

    // Создание новой кафедры
    fun createDepartment(department: Department): Call<Department> {
        return apiService.createDepartment(department)
    }

    // Обновление информации о кафедре
    fun updateDepartment(id: Int, department: Department): Call<Department> {
        return apiService.updateDepartment(id, department)
    }

    // Удаление кафедры
    fun deleteDepartment(id: Int): Call<Void> {
        return apiService.deleteDepartment(id)
    }
}

