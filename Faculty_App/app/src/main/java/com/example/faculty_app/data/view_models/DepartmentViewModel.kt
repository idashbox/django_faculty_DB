package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.repositories.DepartmentRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DepartmentViewModel(private val departmentRepository: DepartmentRepository) : ViewModel() {

    private val _departments = MutableLiveData<List<Department>>()
    val departments: LiveData<List<Department>> get() = _departments

    private val _isDepartmentAdded = MutableLiveData<Boolean>()
    val isDepartmentAdded: LiveData<Boolean> get() = _isDepartmentAdded

    private val _isDepartmentUpdated = MutableLiveData<Boolean>()
    val isDepartmentUpdated: LiveData<Boolean> get() = _isDepartmentUpdated

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    init {
        _pageSize.value = 10
        fetchDepartments(orderBy = "id")
    }

    fun fetchDepartments(
        nameFilter: String? = null,
        orderBy: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = departmentRepository.getDepartments(
                    _currentPage,
                    _pageSize.value ?: 10,
                    nameFilter,
                    orderBy
                )
                if (response.isSuccessful) {
                    val departmentResponse = response.body()
                    _departments.postValue(departmentResponse?.results ?: emptyList())
                } else {
                    Log.e("DepartmentViewModel", "Failed to fetch departments: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DepartmentViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addDepartment(title: String, headId: Int) {
        viewModelScope.launch {
            try {
                val newDepartment = Department(id = 0, title = title, head_of_department = headId)
                val response = departmentRepository.createDepartment(newDepartment)
                _isDepartmentAdded.postValue(response.isSuccessful)
                fetchDepartments(orderBy = "id")
            } catch (e: Exception) {
                Log.e("DepartmentViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun updateDepartment(departmentId: Int, title: String, headId: Int) {
        viewModelScope.launch {
            try {
                val updatedDepartment = Department(id = departmentId, title = title, head_of_department = headId)
                val response = departmentRepository.updateDepartment(departmentId, updatedDepartment)
                _isDepartmentUpdated.postValue(response.isSuccessful)
                fetchDepartments(orderBy = "id")
            } catch (e: Exception) {
                Log.e("DepartmentViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun deleteDepartment(departmentId: Int) {
        viewModelScope.launch {
            try {
                val response = departmentRepository.deleteDepartment(departmentId)
                if (response.isSuccessful) {
                    fetchDepartments(orderBy = "id")
                } else {
                    Log.e("DepartmentViewModel", "Failed to delete department: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DepartmentViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchDepartments(orderBy = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchDepartments(orderBy = "id")
        }
    }
}
