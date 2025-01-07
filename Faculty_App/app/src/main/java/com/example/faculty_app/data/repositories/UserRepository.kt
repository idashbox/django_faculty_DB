package com.example.faculty_app.data.repositories

import com.example.faculty_app.data.models.TeacherResponse
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.models.UserResponse
import com.example.faculty_app.data.network.ApiService
import com.example.faculty_app.data.network.RetrofitClient
import retrofit2.Response

class UserRepository {

    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getUsers(
        page: Int,
        pageSize: Int,
        name: String? = null,
        surname: String? = null,
        middleName: String? = null,
        birthday: String? = null,
        email: String? = null,
        login: String? = null,
        sex: String? = null,
        minAge: Int? = null,
        maxAge: Int? = null,
        ordering: String? = null
    ): Response<UserResponse> {
        return apiService.getUsers(
            page = page,
            pageSize = pageSize,
            orderBy = ordering,
            name = name,
            surname = surname,
            middleName = middleName,
            birthday = birthday,
            email = email,
            login = login,
            sex = sex,
            minAge = minAge,
            maxAge = maxAge
        )
    }

    suspend fun searchUsers(
        page: Int,
        pageSize: Int,
        query: String
    ): Response<UserResponse> {
        return apiService.searchUsers(page, pageSize, query)
    }

    suspend fun getUser(id: Int): Response<User> {
        return apiService.getUser(id)
    }

    suspend fun createUser(user: User): Response<User> {
        return apiService.createUser(user)
    }

    suspend fun updateUser(id: Int, user: User): Response<User> {
        return apiService.updateUser(id, user)
    }

    suspend fun deleteUser(id: Int): Response<Void> {
        return apiService.deleteUser(id)
    }
}
