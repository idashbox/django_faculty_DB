package com.example.faculty_app.data.network

import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("api/users/")
    fun getUsers(): Call<List<User>>

    @GET("api/users/{id}/")
    fun getUser(@Path("id") id: Int): Call<User>

    @POST("api/users/")
    fun createUser(@Body user: User): Call<User>

    @PUT("api/users/{id}/")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("api/users/{id}/")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    @GET("api/teachers/")
    fun getTeachers(): Call<List<Teacher>>

    @GET("api/teachers/{id}")
    fun getTeacher(@Path("id") id: Int): Call<Teacher>

    @PUT("api/teachers/{id}/")
    fun updateTeacher(@Path("id") id: Int, @Body teacher: Teacher): Call<Teacher>

    @POST("api/teachers/")
    fun createTeacher(@Body teacher: Teacher): Call<Teacher>

    @DELETE("api/teachers/{id}/")
    fun deleteTeacher(@Path("id") id: Int): Call<Void>

    @GET("api/departments/{id}/")
    fun getDepartment(@Path("id") id: Int): Call<Department>

    @GET("api/departments/")
    fun getDepartments(): Call<List<Department>>

    @POST("api/departments/")
    fun createDepartment(@Body department: Department): Call<Department>

    @DELETE("api/departments/{id}/")
    fun deleteDepartment(@Path("id") id: Int): Call<Void>

    @PUT("api/departments/{id}/")
    fun updateDepartment(@Path("id") id: Int, @Body department: Department): Call<Department>
}
