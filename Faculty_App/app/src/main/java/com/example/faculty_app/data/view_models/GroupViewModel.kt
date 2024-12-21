package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.repositories.GroupRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupViewModel(private val groupRepository: GroupRepository) : ViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> get() = _groups

    private val _isGroupAdded = MutableLiveData<Boolean>()
    val isGroupAdded: LiveData<Boolean> get() = _isGroupAdded

    private val _isGroupUpdated = MutableLiveData<Boolean>()
    val isGroupUpdated: LiveData<Boolean> get() = _isGroupUpdated

    fun fetchGroups() {
        groupRepository.getGroups().enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) {
                    val sortedList = response.body()?.sortedBy { it.id } ?: emptyList()
                    _groups.postValue(sortedList)
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Log.e("GroupViewModel", "Failed to fetch groups: ${t.message}")
            }
        })
    }

    fun addGroup(group: Group) {
        groupRepository.createGroup(group).enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                _isGroupAdded.postValue(response.isSuccessful)
                fetchGroups()
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                _isGroupAdded.postValue(false)
                Log.e("GroupViewModel", "Failed to add group: ${t.message}")
            }
        })
    }

    fun updateGroup(groupId: Int, group: Group) {
        groupRepository.updateGroup(groupId, group).enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                _isGroupUpdated.postValue(response.isSuccessful)
                fetchGroups()
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                _isGroupUpdated.postValue(false)
                Log.e("GroupViewModel", "Failed to update group: ${t.message}")
            }
        })
    }

    fun deleteGroup(groupId: Int) {
        groupRepository.deleteGroup(groupId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchGroups()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("GroupViewModel", "Failed to delete group: ${t.message}")
            }
        })
    }
}
