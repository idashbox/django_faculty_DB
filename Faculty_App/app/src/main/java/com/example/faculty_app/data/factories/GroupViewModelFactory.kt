package com.example.faculty_app.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.data.view_models.GroupViewModel
import com.example.faculty_app.data.repositories.GroupRepository

class GroupViewModelFactory(private val groupRepository: GroupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            return GroupViewModel(groupRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
