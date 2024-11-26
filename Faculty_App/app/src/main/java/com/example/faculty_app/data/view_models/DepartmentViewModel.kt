package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.repositories.DepartmentRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentViewModel(private val departmentRepository: DepartmentRepository) : ViewModel() {

    private val _departments = MutableLiveData<List<Department>>()
    val departments: LiveData<List<Department>> get() = _departments

    private val _isDepartmentAdded = MutableLiveData<Boolean>()
    val isDepartmentAdded: LiveData<Boolean> get() = _isDepartmentAdded

    private val _isDepartmentUpdated = MutableLiveData<Boolean>()
    val isDepartmentUpdated: LiveData<Boolean> get() = _isDepartmentUpdated

    fun fetchDepartments() {
        departmentRepository.getDepartments().enqueue(object : Callback<List<Department>> {
            override fun onResponse(call: Call<List<Department>>, response: Response<List<Department>>) {
                if (response.isSuccessful) {
                    _departments.postValue(response.body())
                } else {
                    Log.e("DepartmentViewModel", "Failed to fetch departments: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Department>>, t: Throwable) {
                Log.e("DepartmentViewModel", "Failed to fetch departments: ${t.message}")
            }
        })
    }

    fun addDepartment(title: String, headId: Int) {
        val newDepartment = Department(id = 0, title = title, head_of_department = headId)
        departmentRepository.createDepartment(newDepartment).enqueue(object : Callback<Department> {
            override fun onResponse(call: Call<Department>, response: Response<Department>) {
                _isDepartmentAdded.postValue(response.isSuccessful)
                if (response.isSuccessful) {
                    fetchDepartments()
                } else {
                    Log.e("DepartmentViewModel", "Failed to add department: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Department>, t: Throwable) {
                _isDepartmentAdded.postValue(false)
                Log.e("DepartmentViewModel", "Failed to add department: ${t.message}")
            }
        })
    }

    fun updateDepartment(departmentId: Int, title: String, headId: Int) {
        val updatedDepartment = Department(id = departmentId, title = title, head_of_department = headId)
        departmentRepository.updateDepartment(departmentId, updatedDepartment).enqueue(object : Callback<Department> {
            override fun onResponse(call: Call<Department>, response: Response<Department>) {
                _isDepartmentUpdated.postValue(response.isSuccessful)
                if (response.isSuccessful) {
                    fetchDepartments()
                } else {
                    Log.e("DepartmentViewModel", "Failed to update department: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Department>, t: Throwable) {
                _isDepartmentUpdated.postValue(false)
                Log.e("DepartmentViewModel", "Failed to update department: ${t.message}")
            }
        })
    }

    fun deleteDepartment(departmentId: Int) {
        departmentRepository.deleteDepartment(departmentId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchDepartments()
                } else {
                    Log.e("DepartmentViewModel", "Failed to delete department: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("DepartmentViewModel", "Failed to delete department: ${t.message}")
            }
        })
    }
}
