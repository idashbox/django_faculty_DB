package com.example.faculty_app.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.data.view_models.TeacherViewModel
import com.example.faculty_app.data.repositories.TeacherRepository

class TeacherViewModelFactory(private val teacherRepository: TeacherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherViewModel::class.java)) {
            return TeacherViewModel(teacherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}