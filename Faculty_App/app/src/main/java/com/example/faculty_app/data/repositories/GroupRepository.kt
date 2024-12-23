package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.models.DirectionResponse
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient.retrofit
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.GroupResponse
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.models.UserToGroupResponse
import retrofit2.Response

class GroupRepository {

    private val apiService: ApiService

    init {
        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getGroups(
        page: Int,
        pageSize: Int,
        number: String?,
        orderBy: String? = null
    ): Response<GroupResponse> {
        return apiService.getGroups(page, pageSize, number, orderBy)
    }


    suspend fun getGroup(id: Int): Response<Group> {
        return apiService.getGroup(id)
    }

    suspend fun createGroup(group: Group): Response<Group> {
        return apiService.createGroup(group)
    }

    suspend fun updateGroup(id: Int, group: Group): Response<Group> {
        return apiService.updateGroup(id, group)
    }

    suspend fun deleteGroup(id: Int): Response<Void> {
        return apiService.deleteGroup(id)
    }

    suspend fun getDirection(id: Int): Response<Direction> {
        return apiService.getDirection(id)
    }

    suspend fun getUsersToGroup(
        page: Int,
        pageSize: Int,
        name: String? = null,
        orderBy: String? = null
    ): Response<UserToGroupResponse> {
        return apiService.getUserToGroups(page, pageSize, name, orderBy)
    }


}
