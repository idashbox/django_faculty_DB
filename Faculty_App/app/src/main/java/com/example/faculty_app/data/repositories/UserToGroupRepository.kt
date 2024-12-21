package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.UserToGroup
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserToGroupRepository {

    private val apiService: ApiService
    init {
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getUserToGroups(): Call<List<UserToGroup>> {
        return apiService.getUserToGroups()
    }

    fun getUserToGroup(id: Int): Call<UserToGroup> {
        return apiService.getUserToGroup(id)
    }

    fun createUserToGroup(userToGroup: UserToGroup): Call<UserToGroup> {
        return apiService.createUserToGroup(userToGroup)
    }

    fun updateUserToGroup(id: Int, userToGroup: UserToGroup): Call<UserToGroup> {
        return apiService.updateUserToGroup(id, userToGroup)
    }

    fun deleteUserToGroup(id: Int): Call<Void> {
        return apiService.deleteUserToGroup(id)
    }
}
