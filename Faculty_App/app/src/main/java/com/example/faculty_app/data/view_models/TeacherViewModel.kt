package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.Department
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

    private val _departments = MutableLiveData<List<Department>>()
    val departments: LiveData<List<Department>> get() = _departments

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    init {
        _pageSize.value = 10
        fetchTeachers(ordering = "id")
        fetchStatistics()
    }

    // Переменные для хранения текущих фильтров
    private var currentNameFilter: String? = null
    private var currentSurnameFilter: String? = null
    private var currentMiddleNameFilter: String? = null
    private var currentBirthdayFilter: String? = null
    private var currentDepartmentFilter: Int? = null
    private var currentOrderBy: String? = "id"

    init {
        _pageSize.value = 10
        fetchTeachers()
        fetchStatistics()
    }

    fun fetchTeachers(
        name: String? = null,
        surname: String? = null,
        middleName: String? = null,
        birthday: String? = null,
        email: String? = null,
        login: String? = null,
        sex: String? = null,
        department: Int? = null,
        yearOfStartOfWork: String? = null,
        minAge: Int? = null,
        maxAge: Int? = null,
        minYearStart: Int? = null,
        maxYearStart: Int? = null,
        ordering: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.getTeachers(
                    page = _currentPage,
                    pageSize = _pageSize.value ?: 10,
                    name = name,
                    surname = surname,
                    middleName = middleName,
                    birthday = birthday,
                    email = email,
                    login = login,
                    sex = sex,
                    department = department,
                    yearOfStartOfWork = yearOfStartOfWork,
                    minAge = minAge,
                    maxAge = maxAge,
                    minYearStart = minYearStart,
                    maxYearStart = maxYearStart,
                    ordering = ordering
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


    fun setFilter(
        name: String? = null,
        surname: String? = null,
        middleName: String? = null,
        birthday: String? = null,
        departmentId: Int? = null,
        orderBy: String? = null
    ) {
        currentNameFilter = name
        currentSurnameFilter = surname
        currentMiddleNameFilter = middleName
        currentBirthdayFilter = birthday
        currentDepartmentFilter = departmentId
        currentOrderBy = orderBy

        // Сбрасываем на первую страницу при изменении фильтра
        _currentPage = 1
        fetchTeachers()
    }

    fun clearFilters() {
        currentNameFilter = null
        currentSurnameFilter = null
        currentMiddleNameFilter = null
        currentBirthdayFilter = null
        currentDepartmentFilter = null
        currentOrderBy = "id"

        _currentPage = 1
        fetchTeachers()
    }

    fun addTeacher(teacher: Teacher) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.createTeacher(teacher)
                _isTeacherAdded.postValue(response.isSuccessful)
                fetchTeachers(ordering = "id")
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
                fetchTeachers(ordering = "id")
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
                    fetchTeachers(ordering = "id")
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
        fetchTeachers(ordering = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchTeachers(ordering = "id")
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

    fun searchTeachers(query: String) {
        viewModelScope.launch {
            try {
                val response = teacherRepository.searchTeachers(
                    _currentPage,
                    _pageSize.value ?: 10,
                    query
                )
                if (response.isSuccessful) {
                    val teacherResponse = response.body()
                    _teachers.postValue(teacherResponse?.results ?: emptyList())
                } else {
                    Log.e("TeacherViewModel", "Failed to search teachers: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun fetchDepartments() {
        viewModelScope.launch {
            try {
                val response = teacherRepository.getDepartments(1, 100)
                if (response.isSuccessful) {
                    val departmentResponse = response.body()
                    _departments.postValue(departmentResponse?.results ?: emptyList())
                } else {
                    Log.e("TeacherViewModel", "Failed to fetch departments: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeacherViewModel", "Exception occurred: ${e.message}")
            }
        }
    }
}
