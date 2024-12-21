package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.TeacherStatistics
import com.example.faculty_app.data.repositories.TeacherRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TeacherViewModel(private val teacherRepository: TeacherRepository) : ViewModel() {

    private val _teachers = MutableLiveData<List<Teacher>>()
    val teachers: LiveData<List<Teacher>> get() = _teachers

    private val _isTeacherAdded = MutableLiveData<Boolean>()
    val isTeacherAdded: LiveData<Boolean> get() = _isTeacherAdded

    private val _isTeacherUpdated = MutableLiveData<Boolean>()
    val isTeacherUpdated: LiveData<Boolean> get() = _isTeacherUpdated

    private val _statistics = MutableLiveData<TeacherStatistics>()
    val statistics: LiveData<TeacherStatistics> get() = _statistics

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    init {
        _pageSize.value = 10
        fetchTeachers(orderBy = "id")
        fetchStatistics()
    }

    fun fetchTeachers(
        nameFilter: String? = null,
        departmentFilter: Int? = null,
        orderBy: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.getTeachers(
                    _currentPage,
                    _pageSize.value ?: 10,
                    nameFilter,
                    departmentFilter,
                    orderBy
                )
                if (response.isSuccessful) {
                    val teacherResponse = response.body()
                    _teachers.postValue(teacherResponse?.results ?: emptyList())
                } else {
                    Log.e("TeacherViewModel", "Failed to fetch teachers: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addTeacher(teacher: Teacher) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.createTeacher(teacher)
                _isTeacherAdded.postValue(response.isSuccessful)
                fetchTeachers(orderBy = "id")
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun updateTeacher(teacherId: Int, teacher: Teacher) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.updateTeacher(teacherId, teacher)
                _isTeacherUpdated.postValue(response.isSuccessful)
                fetchTeachers(orderBy = "id")
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun deleteTeacher(teacherId: Int) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.deleteTeacher(teacherId)
                if (response.isSuccessful) {
                    fetchTeachers(orderBy = "id")
                } else {
                    Log.e("TeacherViewModel", "Failed to delete teacher: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchTeachers(orderBy = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchTeachers(orderBy = "id")
        }
    }

    private fun fetchStatistics() {
        viewModelScope.launch {
            try {
                val response = teacherRepository.getStatistics()
                if (response.isSuccessful) {
                    _statistics.postValue(response.body())
                } else {
                    Log.e("TeacherViewModel", "Failed to fetch statistics: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }
}
