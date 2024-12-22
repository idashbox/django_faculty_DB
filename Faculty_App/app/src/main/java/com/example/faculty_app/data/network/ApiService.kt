package com.example.faculty_app.data.network

import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.DepartmentResponse
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.TeacherResponse
import com.example.faculty_app.data.models.TeacherStatistics
import com.example.faculty_app.data.models.UserResponse
import com.example.faculty_app.data.models.UserToGroup
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
interface ApiService {

    @GET("api/teachers/")
    suspend fun getTeachers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("name") name: String?,
        @Query("surname") surname: String?,
        @Query("middle_name") middleName: String?,
        @Query("birthday") birthday: String?,
        @Query("department") department: Int?,
        @Query("orderBy") orderBy: String?
    ): Response<TeacherResponse>

    @GET("teachers/search/")
    suspend fun searchTeachers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("query") query: String
    ): Response<TeacherResponse>

    @GET("api/teachers/{id}/")
    suspend fun getTeacher(@Path("id") id: Int): Response<Teacher>

    @POST("api/teachers/")
    suspend fun createTeacher(@Body teacher: Teacher): Response<Teacher>

    @PUT("api/teachers/{id}/")
    suspend fun updateTeacher(@Path("id") id: Int, @Body teacher: Teacher): Response<Teacher>

    @DELETE("api/teachers/{id}/")
    suspend fun deleteTeacher(@Path("id") id: Int): Response<Void>

    @GET("api/statistics/")
    suspend fun getStatistics(): Response<TeacherStatistics>

    @GET("api/users/")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("name") name: String? = null,
        @Query("orderBy") orderBy: String? = null
    ): Response<UserResponse>

    @GET("api/users/{id}/")
    suspend fun getUser(@Path("id") id: Int): Response<User>

    @POST("api/users/")
    suspend fun createUser(@Body user: User): Response<User>

    @PUT("api/users/{id}/")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>

    @DELETE("api/users/{id}/")
    suspend fun deleteUser(@Path("id") id: Int): Response<Void>

    @GET("api/departments/{id}/")
    suspend fun getDepartment(@Path("id") id: Int): Response<Department>

    @GET("api/departments/")
    suspend fun getDepartments(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("title") title: String? = null,
        @Query("orderBy") orderBy: String? = null
    ): Response<DepartmentResponse>

    @POST("api/departments/")
    suspend fun createDepartment(@Body department: Department): Response<Department>

    @DELETE("api/departments/{id}/")
    suspend fun deleteDepartment(@Path("id") id: Int): Response<Void>

    @PUT("api/departments/{id}/")
    suspend fun updateDepartment(@Path("id") id: Int, @Body department: Department): Response<Department>

    @GET("api/directions/")
    fun getDirections(): Call<List<Direction>>

    @GET("api/directions/{id}/")
    fun getDirection(@Path("id") id: Int): Call<Direction>

    @POST("api/directions/")
    fun createDirection(@Body direction: Direction): Call<Direction>

    @PUT("api/directions/{id}/")
    fun updateDirection(@Path("id") id: Int, @Body direction: Direction): Call<Direction>

    @DELETE("api/directions/{id}/")
    fun deleteDirection(@Path("id") id: Int): Call<Void>

    @GET("api/groups/")
    fun getGroups(): Call<List<Group>>

    @GET("api/groups/{id}/")
    fun getGroup(@Path("id") id: Int): Call<Group>

    @POST("api/groups/")
    fun createGroup(@Body group: Group): Call<Group>

    @PUT("api/groups/{id}/")
    fun updateGroup(@Path("id") id: Int, @Body group: Group): Call<Group>

    @DELETE("api/groups/{id}/")
    fun deleteGroup(@Path("id") id: Int): Call<Void>

    @GET("api/user_to_groups/")
    fun getUserToGroups(): Call<List<UserToGroup>>

    @GET("api/user_to_groups/{id}/")
    fun getUserToGroup(@Path("id") id: Int): Call<UserToGroup>

    @POST("api/user_to_groups/")
    fun createUserToGroup(@Body userToGroup: UserToGroup): Call<UserToGroup>

    @PUT("api/user_to_groups/{id}/")
    fun updateUserToGroup(@Path("id") id: Int, @Body userToGroup: UserToGroup): Call<UserToGroup>

    @DELETE("api/user_to_groups/{id}/")
    fun deleteUserToGroup(@Path("id") id: Int): Call<Void>
}
