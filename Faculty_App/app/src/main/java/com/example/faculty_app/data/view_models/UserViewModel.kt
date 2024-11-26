package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _isUserAdded = MutableLiveData<Boolean>()
    val isUserAdded: LiveData<Boolean> get() = _isUserAdded

    private val _isUserUpdated = MutableLiveData<Boolean>()
    val isUserUpdated: LiveData<Boolean> get() = _isUserUpdated

    fun fetchUsers() {
        userRepository.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val sortedList = response.body()?.sortedBy { it.id } ?: emptyList()
                    _users.postValue(sortedList)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Handle failure
                Log.e("UserViewModel", "Failed to fetch users: ${t.message}")
            }
        })
    }

    fun addUser(user: User) {
        userRepository.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isUserAdded.postValue(response.isSuccessful)
                fetchUsers()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isUserAdded.postValue(false)
                Log.e("UserViewModel", "Failed to add user: ${t.message}")
            }
        })
    }

    fun updateUser(userId: Int, user: User) {
        userRepository.updateUser(userId, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isUserUpdated.postValue(response.isSuccessful)
                fetchUsers()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isUserUpdated.postValue(false)
                Log.e("UserViewModel", "Failed to update user: ${t.message}")
            }
        })
    }

    fun deleteUser(userId: Int) {
        userRepository.deleteUser(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchUsers()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
                Log.e("UserViewModel", "Failed to delete user: ${t.message}")
            }
        })
    }
}
