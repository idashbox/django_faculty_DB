package com.example.faculty_app.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.data.view_models.UserToGroupViewModel
import com.example.faculty_app.data.repositories.UserToGroupRepository

class UserToGroupViewModelFactory(private val userToGroupRepository: UserToGroupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserToGroupViewModel::class.java)) {
            return UserToGroupViewModel(userToGroupRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
