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
        fetchUsers(orderBy = "id")
    }

    fun fetchUsers(
        nameFilter: String? = null,
        orderBy: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = userRepository.getUsers(
                    _currentPage,
                    _pageSize.value ?: 10,
                    nameFilter,
                    orderBy
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
                fetchUsers(orderBy = "id")
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
                fetchUsers(orderBy = "id")
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
                    fetchUsers(orderBy = "id")
                } else {
                    Log.e("UserViewModel", "Failed to delete user: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchUsers(orderBy = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchUsers(orderBy = "id")
        }
    }
}
