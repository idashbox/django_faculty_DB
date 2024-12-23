package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.repositories.DirectionRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DirectionViewModel(private val directionRepository: DirectionRepository) : ViewModel() {

    private val _directions = MutableLiveData<List<Direction>>()
    val directions: LiveData<List<Direction>> get() = _directions

    private val _isDirectionAdded = MutableLiveData<Boolean>()
    val isDirectionAdded: LiveData<Boolean> get() = _isDirectionAdded

    private val _isDirectionUpdated = MutableLiveData<Boolean>()
    val isDirectionUpdated: LiveData<Boolean> get() = _isDirectionUpdated

    private val _departments = MutableLiveData<List<Department>>()
    val departments: LiveData<List<Department>> get() = _departments

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    init {
        _pageSize.value = 10
        fetchDirections(orderBy = "id")
    }

    fun fetchDirections(
        nameFilter: String? = null,
        orderBy: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = directionRepository.getDirections(
                    _currentPage,
                    _pageSize.value ?: 10,
                    nameFilter,
                    orderBy
                )
                if (response.isSuccessful) {
                    val directionResponse = response.body()
                    _directions.postValue(directionResponse?.results ?: emptyList())
                } else {
                    Log.e("DirectionViewModel", "Failed to fetch directions: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DirectionViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addDirection(direction: Direction) {
        viewModelScope.launch {
            try {
                val response = directionRepository.createDirection(direction)
                _isDirectionAdded.postValue(response.isSuccessful)
                fetchDirections(orderBy = "id")
            } catch (e: Exception) {
                Log.e("DirectionViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun updateDirection(directionId: Int, direction: Direction) {
        viewModelScope.launch {
            try {
                val response = directionRepository.updateDirection(directionId, direction)
                _isDirectionUpdated.postValue(response.isSuccessful)
                fetchDirections(orderBy = "id")
            } catch (e: Exception) {
                Log.e("DirectionViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun deleteDirection(directionId: Int) {
        viewModelScope.launch {
            try {
                val response = directionRepository.deleteDirection(directionId)
                if (response.isSuccessful) {
                    fetchDirections(orderBy = "id")
                } else {
                    Log.e("DirectionViewModel", "Failed to delete direction: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DirectionViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchDirections(orderBy = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchDirections(orderBy = "id")
        }
    }

    fun fetchDepartments() {
        viewModelScope.launch {
            try {
                val response = directionRepository.getDepartments(1, 100)
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
