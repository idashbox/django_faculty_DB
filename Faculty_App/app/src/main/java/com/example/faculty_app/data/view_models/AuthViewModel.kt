package com.example.faculty_app.data.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.AuthResponse
import com.example.faculty_app.data.models.AuthToken
import com.example.faculty_app.data.repositories.AuthRepository
import com.example.faculty_app.data.repositories.DepartmentRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val repository: AuthRepository): ViewModel() {


    val authToken = MutableLiveData<AuthToken?>()
    val errorMessage = MutableLiveData<String?>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response: Response<AuthToken> = repository.login(username, password)
            if (response.isSuccessful) {
                authToken.postValue(response.body())
            } else {
                errorMessage.postValue("Login failed: ${response.errorBody()?.string()}")
            }
        }
    }
}
