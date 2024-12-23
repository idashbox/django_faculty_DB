package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.repositories.UserToGroupRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class UserToGroupViewModel(private val userToGroupRepository: UserToGroupRepository) : ViewModel() {

    private val _userToGroups = MutableLiveData<List<UserToGroup>>()
    val userToGroups: LiveData<List<UserToGroup>> get() = _userToGroups

    private val _isUserToGroupAdded = MutableLiveData<Boolean>()
    val isUserToGroupAdded: LiveData<Boolean> get() = _isUserToGroupAdded

    private val _isUserToGroupUpdated = MutableLiveData<Boolean>()
    val isUserToGroupUpdated: LiveData<Boolean> get() = _isUserToGroupUpdated

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> get() = _groups

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    init {
        _pageSize.value = 10
        fetchUserToGroups(orderBy = "id")
    }

    fun fetchUserToGroups(
        nameFilter: String? = null,
        orderBy: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = userToGroupRepository.getUserToGroups(
                    _currentPage,
                    _pageSize.value ?: 10,
                    nameFilter,
                    orderBy
                )
                if (response.isSuccessful) {
                    val userToGroupResponse = response.body()
                    _userToGroups.postValue(userToGroupResponse?.results ?: emptyList())
                } else {
                    Log.e("UserToGroupViewModel", "Failed to fetch user to groups: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserToGroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addUserToGroup(userToGroup: UserToGroup) {
        viewModelScope.launch {
            try {
                val response = userToGroupRepository.createUserToGroup(userToGroup)
                _isUserToGroupAdded.postValue(response.isSuccessful)
                fetchUserToGroups(orderBy = "id")
            } catch (e: Exception) {
                Log.e("UserToGroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun updateUserToGroup(userToGroupId: Int, userToGroup: UserToGroup) {
        viewModelScope.launch {
            try {
                val response = userToGroupRepository.updateUserToGroup(userToGroupId, userToGroup)
                _isUserToGroupUpdated.postValue(response.isSuccessful)
                fetchUserToGroups(orderBy = "id")
            } catch (e: Exception) {
                Log.e("UserToGroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun deleteUserToGroup(userToGroupId: Int) {
        viewModelScope.launch {
            try {
                val response = userToGroupRepository.deleteUserToGroup(userToGroupId)
                if (response.isSuccessful) {
                    fetchUserToGroups(orderBy = "id")
                } else {
                    Log.e("UserToGroupViewModel", "Failed to delete user to group: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserToGroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchUserToGroups(orderBy = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchUserToGroups(orderBy = "id")
        }
    }

    fun fetchGroups() {
        viewModelScope.launch {
            try {
                val response = userToGroupRepository.getGroups(1, 100)
                if (response.isSuccessful) {
                    val departmentResponse = response.body()
                    _groups.postValue(departmentResponse?.results ?: emptyList())
                } else {
                    Log.e("TeacherViewModel", "Failed to fetch departments: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }
}
