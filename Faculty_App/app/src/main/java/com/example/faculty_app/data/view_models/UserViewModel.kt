package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _isUserAdded = MutableLiveData<Boolean>()
    val isUserAdded: LiveData<Boolean> get() = _isUserAdded

    private val _isUserUpdated = MutableLiveData<Boolean>()
    val isUserUpdated: LiveData<Boolean> get() = _isUserUpdated

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    init {
        _pageSize.value = 10
        fetchUsers(ordering = "id")
    }

    fun fetchUsers(
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
    ) {
        viewModelScope.launch {
            try {
                val response = userRepository.getUsers(
                    page = _currentPage,
                    pageSize = _pageSize.value ?: 10,
                    name = name,
                    surname = surname,
                    middleName = middleName,
                    birthday = birthday,
                    email = email,
                    login = login,
                    sex = sex,
                    minAge = minAge,
                    maxAge = maxAge,
                    ordering = ordering
                )
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    _users.postValue(userResponse?.results ?: emptyList())
                } else {
                    Log.e("UserViewModel", "Failed to fetch users: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                val response = userRepository.createUser(user)
                _isUserAdded.postValue(response.isSuccessful)
                fetchUsers(ordering = "id")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun updateUser(userId: Int, user: User) {
        viewModelScope.launch {
            try {
                val response = userRepository.updateUser(userId, user)
                _isUserUpdated.postValue(response.isSuccessful)
                fetchUsers(ordering = "id")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = userRepository.deleteUser(userId)
                if (response.isSuccessful) {
                    fetchUsers(ordering = "id")
                } else {
                    Log.e("UserViewModel", "Failed to delete user: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.searchUsers(
                    _currentPage,
                    _pageSize.value ?: 10,
                    query
                )
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    _users.postValue(userResponse?.results ?: emptyList())
                } else {
                    Log.e("USerViewModel", "Failed to search users: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchUsers(ordering = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchUsers(ordering = "id")
        }
    }
}
