package com.example.faculty_app.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.data.view_models.DirectionViewModel
import com.example.faculty_app.data.repositories.DirectionRepository

class DirectionViewModelFactory(private val directionRepository: DirectionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DirectionViewModel::class.java)) {
            return DirectionViewModel(directionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
