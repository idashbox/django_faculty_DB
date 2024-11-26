package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.repositories.TeacherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherViewModel(private val teacherRepository: TeacherRepository) : ViewModel() {

    private val _teachers = MutableLiveData<List<Teacher>>()
    val teachers: LiveData<List<Teacher>> get() = _teachers

    private val _isTeacherAdded = MutableLiveData<Boolean>()
    val isTeacherAdded: LiveData<Boolean> get() = _isTeacherAdded

    private val _isTeacherUpdated = MutableLiveData<Boolean>()
    val isTeacherUpdated: LiveData<Boolean> get() = _isTeacherUpdated

    fun fetchTeachers() {
        teacherRepository.getTeachers().enqueue(object : Callback<List<Teacher>> {
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if (response.isSuccessful) {
                    _teachers.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                // Handle failure
                Log.e("TeacherViewModel", "Failed to fetch teachers: ${t.message}")
            }
        })
    }

    fun addTeacher(teacher: Teacher) {
        teacherRepository.createTeacher(teacher).enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                _isTeacherAdded.postValue(response.isSuccessful)
                fetchTeachers()
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
                _isTeacherAdded.postValue(false)
                Log.e("TeacherViewModel", "Failed to add teacher: ${t.message}")
            }
        })
    }

    fun updateTeacher(teacherId: Int, teacher: Teacher) {
        teacherRepository.updateTeacher(teacherId, teacher).enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                _isTeacherUpdated.postValue(response.isSuccessful)
                fetchTeachers()
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
                _isTeacherUpdated.postValue(false)
                Log.e("TeacherViewModel", "Failed to update teacher: ${t.message}")
            }
        })
    }

    fun deleteTeacher(teacherId: Int) {
        teacherRepository.deleteTeacher(teacherId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchTeachers()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
                Log.e("TeacherViewModel", "Failed to delete teacher: ${t.message}")
            }
        })
    }
}



