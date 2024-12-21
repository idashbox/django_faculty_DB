package com.example.faculty_app.data.network

import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.TeacherResponse
import com.example.faculty_app.data.models.TeacherStatistics
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
        @Query("user/name") name: String? = null,
        @Query("department") department: Int? = null,
        @Query("orderBy") orderBy: String? = null
    ): Response<TeacherResponse>

    @GET("api/teachers/{id}")
    suspend fun getTeacher(@Path("id") id: Int): Response<Teacher>

    @POST("api/teachers/")
    suspend fun createTeacher(@Body teacher: Teacher): Response<Teacher>

    @PUT("api/teachers/{id}")
    suspend fun updateTeacher(@Path("id") id: Int, @Body teacher: Teacher): Response<Teacher>

    @DELETE("api/teachers/{id}")
    suspend fun deleteTeacher(@Path("id") id: Int): Response<Void>

    @GET("api/statistics/")
    suspend fun getStatistics(): Response<TeacherStatistics>

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

//    @GET("api/teachers/")
//    fun getTeachers(): Call<List<Teacher>>

//    @GET("api/teachers/{id}")
//    fun getTeacher(@Path("id") id: Int): Call<Teacher>
//
//    @PUT("api/teachers/{id}/")
//    fun updateTeacher(@Path("id") id: Int, @Body teacher: Teacher): Call<Teacher>
//
//    @POST("api/teachers/")
//    fun createTeacher(@Body teacher: Teacher): Call<Teacher>
//
//    @DELETE("api/teachers/{id}/")
//    fun deleteTeacher(@Path("id") id: Int): Call<Void>

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
