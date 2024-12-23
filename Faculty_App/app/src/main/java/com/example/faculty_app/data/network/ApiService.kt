package com.example.faculty_app.data.network

import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.DepartmentResponse
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.models.DirectionResponse
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.GroupResponse
import com.example.faculty_app.data.models.TeacherResponse
import com.example.faculty_app.data.models.TeacherStatistics
import com.example.faculty_app.data.models.UserResponse
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.models.UserToGroupResponse
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
    suspend fun getDirections(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("title") title: String? = null,
        @Query("orderBy") orderBy: String? = null
    ): Response<DirectionResponse>

    @GET("api/directions/{id}/")
    suspend fun getDirection(@Path("id") id: Int): Response<Direction>

    @POST("api/directions/")
    suspend fun createDirection(@Body direction: Direction): Response<Direction>

    @PUT("api/directions/{id}/")
    suspend fun updateDirection(@Path("id") id: Int, @Body direction: Direction): Response<Direction>

    @DELETE("api/directions/{id}/")
    suspend fun deleteDirection(@Path("id") id: Int): Response<Void>

    @GET("api/groups/")
    suspend fun getGroups(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("number") number: String? = null,
        @Query("orderBy") orderBy: String? = null
    ): Response<GroupResponse>

    @GET("api/groups/{id}/")
    suspend fun getGroup(@Path("id") id: Int): Response<Group>

    @POST("api/groups/")
    suspend fun createGroup(@Body group: Group): Response<Group>

    @PUT("api/groups/{id}/")
    suspend fun updateGroup(@Path("id") id: Int, @Body group: Group): Response<Group>

    @DELETE("api/groups/{id}/")
    suspend fun deleteGroup(@Path("id") id: Int): Response<Void>

    @GET("api/user_to_groups/")
    suspend fun getUserToGroups(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("name") name: String? = null,
        @Query("orderBy") orderBy: String? = null
    ): Response<UserToGroupResponse>

    @GET("api/user_to_groups/{id}/")
    suspend fun getUserToGroup(@Path("id") id: Int): Response<UserToGroup>

    @POST("api/user_to_groups/")
    suspend fun createUserToGroup(@Body userToGroup: UserToGroup): Response<UserToGroup>

    @PUT("api/user_to_groups/{id}/")
    suspend fun updateUserToGroup(@Path("id") id: Int, @Body userToGroup: UserToGroup): Response<UserToGroup>

    @DELETE("api/user_to_groups/{id}/")
    suspend fun deleteUserToGroup(@Path("id") id: Int): Response<Void>
}
