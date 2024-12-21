package com.example.faculty_app.data.view_models


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.repositories.DirectionRepository
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
}
