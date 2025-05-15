package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.DepartmentResponse
import com.example.faculty_app.data.models.GroupResponse
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.models.UserToGroupResponse
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class UserToGroupRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getUserToGroups(
        page: Int,
        pageSize: Int,
        name: String? = null,
        orderBy: String? = null
    ): Response<UserToGroupResponse> {
        return apiService.getUserToGroups(page, pageSize, name, orderBy)
    }

    suspend fun getUserToGroup(id: Int): Response<UserToGroup> {
        return apiService.getUserToGroup(id)
    }

    suspend fun createUserToGroup(userToGroup: UserToGroup): Response<UserToGroup> {
        return apiService.createUserToGroup(userToGroup)
    }

    suspend fun updateUserToGroup(id: Int, userToGroup: UserToGroup): Response<UserToGroup> {
        return apiService.updateUserToGroup(id, userToGroup)
    }

    suspend  fun deleteUserToGroup(id: Int): Response<Void> {
        return apiService.deleteUserToGroup(id)
    }

    suspend fun getGroups(
        page: Int,
        pageSize: Int,
        number: String? = null,
        orderBy: String? = null
    ): Response<GroupResponse> {
        return apiService.getGroups(page, pageSize, number, orderBy)
    }
}
