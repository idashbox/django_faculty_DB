package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.repositories.UserToGroupRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserToGroupViewModel(private val userToGroupRepository: UserToGroupRepository) : ViewModel() {

    private val _userToGroups = MutableLiveData<List<UserToGroup>>()
    val userToGroups: LiveData<List<UserToGroup>> get() = _userToGroups

    private val _isUserToGroupAdded = MutableLiveData<Boolean>()
    val isUserToGroupAdded: LiveData<Boolean> get() = _isUserToGroupAdded

    private val _isUserToGroupUpdated = MutableLiveData<Boolean>()
    val isUserToGroupUpdated: LiveData<Boolean> get() = _isUserToGroupUpdated

    fun fetchUserToGroups() {
        userToGroupRepository.getUserToGroups().enqueue(object : Callback<List<UserToGroup>> {
            override fun onResponse(call: Call<List<UserToGroup>>, response: Response<List<UserToGroup>>) {
                if (response.isSuccessful) {
                    val sortedList = response.body()?.sortedBy { it.id } ?: emptyList()
                    _userToGroups.postValue(sortedList)
                }
            }

            override fun onFailure(call: Call<List<UserToGroup>>, t: Throwable) {
                Log.e("UserToGroupViewModel", "Failed to fetch user to groups: ${t.message}")
            }
        })
    }

    fun addUserToGroup(userToGroup: UserToGroup) {
        userToGroupRepository.createUserToGroup(userToGroup).enqueue(object : Callback<UserToGroup> {
            override fun onResponse(call: Call<UserToGroup>, response: Response<UserToGroup>) {
                _isUserToGroupAdded.postValue(response.isSuccessful)
                fetchUserToGroups()
            }

            override fun onFailure(call: Call<UserToGroup>, t: Throwable) {
                _isUserToGroupAdded.postValue(false)
                Log.e("UserToGroupViewModel", "Failed to add user to group: ${t.message}")
            }
        })
    }

    fun updateUserToGroup(userToGroupId: Int, userToGroup: UserToGroup) {
        userToGroupRepository.updateUserToGroup(userToGroupId, userToGroup).enqueue(object : Callback<UserToGroup> {
            override fun onResponse(call: Call<UserToGroup>, response: Response<UserToGroup>) {
                _isUserToGroupUpdated.postValue(response.isSuccessful)
                fetchUserToGroups()
            }

            override fun onFailure(call: Call<UserToGroup>, t: Throwable) {
                _isUserToGroupUpdated.postValue(false)
                Log.e("UserToGroupViewModel", "Failed to update user to group: ${t.message}")
            }
        })
    }

    fun deleteUserToGroup(userToGroupId: Int) {
        userToGroupRepository.deleteUserToGroup(userToGroupId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchUserToGroups()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UserToGroupViewModel", "Failed to delete user to group: ${t.message}")
            }
        })
    }
}
