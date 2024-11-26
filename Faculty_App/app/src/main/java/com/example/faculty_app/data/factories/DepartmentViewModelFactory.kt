package com.example.faculty_app.data.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.data.view_models.DepartmentViewModel
import com.example.faculty_app.data.repositories.DepartmentRepository

class DepartmentViewModelFactory(
    private val departmentRepository: DepartmentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepartmentViewModel::class.java)) {
            return DepartmentViewModel(departmentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
