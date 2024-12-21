package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.Group
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GroupRepository {

    private val apiService: ApiService
    init {
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getGroups(): Call<List<Group>> {
        return apiService.getGroups()
    }

    fun getGroup(id: Int): Call<Group> {
        return apiService.getGroup(id)
    }

    fun createGroup(group: Group): Call<Group> {
        return apiService.createGroup(group)
    }

    fun updateGroup(id: Int, group: Group): Call<Group> {
        return apiService.updateGroup(id, group)
    }

    fun deleteGroup(id: Int): Call<Void> {
        return apiService.deleteGroup(id)
    }
}
