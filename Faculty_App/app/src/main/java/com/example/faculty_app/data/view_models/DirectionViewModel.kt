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
import retrofit2.Call
import retrofit2.Callback
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

    fun fetchDirections() {
        directionRepository.getDirections().enqueue(object : Callback<List<Direction>> {
            override fun onResponse(call: Call<List<Direction>>, response: Response<List<Direction>>) {
                if (response.isSuccessful) {
                    val sortedList = response.body()?.sortedBy { it.id } ?: emptyList()
                    _directions.postValue(sortedList)
                }
            }

            override fun onFailure(call: Call<List<Direction>>, t: Throwable) {
                Log.e("DirectionViewModel", "Failed to fetch directions: ${t.message}")
            }
        })
    }

    fun addDirection(direction: Direction) {
        directionRepository.createDirection(direction).enqueue(object : Callback<Direction> {
            override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                _isDirectionAdded.postValue(response.isSuccessful)
                fetchDirections()
            }

            override fun onFailure(call: Call<Direction>, t: Throwable) {
                _isDirectionAdded.postValue(false)
                Log.e("DirectionViewModel", "Failed to add direction: ${t.message}")
            }
        })
    }

    fun updateDirection(directionId: Int, direction: Direction) {
        directionRepository.updateDirection(directionId, direction).enqueue(object : Callback<Direction> {
            override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                _isDirectionUpdated.postValue(response.isSuccessful)
                fetchDirections()
            }

            override fun onFailure(call: Call<Direction>, t: Throwable) {
                _isDirectionUpdated.postValue(false)
                Log.e("DirectionViewModel", "Failed to update direction: ${t.message}")
            }
        })
    }

    fun deleteDirection(directionId: Int) {
        directionRepository.deleteDirection(directionId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchDirections()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("DirectionViewModel", "Failed to delete direction: ${t.message}")
            }
        })
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
