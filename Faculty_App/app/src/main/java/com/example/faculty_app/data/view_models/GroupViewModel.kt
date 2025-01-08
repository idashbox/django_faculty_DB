package com.example.faculty_app.data.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.repositories.GroupRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class GroupViewModel(private val groupRepository: GroupRepository) : ViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>> get() = _groups

    private val _isGroupAdded = MutableLiveData<Boolean>()
    val isGroupAdded: LiveData<Boolean> get() = _isGroupAdded

    private val _isGroupUpdated = MutableLiveData<Boolean>()
    val isGroupUpdated: LiveData<Boolean> get() = _isGroupUpdated

    private var _currentPage = 1
    val currentPage: Int get() = _currentPage

    private val _pageSize = MutableLiveData<Int>()
    val pageSize: LiveData<Int> get() = _pageSize

    private var directionCallback: ((String) -> Unit)? = null

    private val _students = MutableLiveData<List<UserToGroup>>()
    val students: LiveData<List<UserToGroup>> get() = _students

    private val _teachers = MutableLiveData<List<Teacher>>()
    val teachers: LiveData<List<Teacher>> get() = _teachers

    private val _directions = MutableLiveData<List<Direction>>()
    val directions: LiveData<List<Direction>> get() = _directions


    init {
        _pageSize.value = 10
        fetchGroups(orderBy = "id")
    }

    fun fetchGroups(
        nameFilter: String? = null,
        orderBy: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = groupRepository.getGroups(
                    _currentPage,
                    _pageSize.value ?: 10,
                    nameFilter,
                    orderBy
                )
                if (response.isSuccessful) {
                    val groupResponse = response.body()
                    _groups.postValue(groupResponse?.results ?: emptyList())
                } else {
                    Log.e("GroupViewModel", "Failed to fetch groups: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun addGroup(group: Group) {
        viewModelScope.launch {
            try {
                val response = groupRepository.createGroup(group)
                _isGroupAdded.postValue(response.isSuccessful)
                fetchGroups(orderBy = "id")
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun updateGroup(groupId: Int, group: Group) {
        viewModelScope.launch {
            try {
                val response = groupRepository.updateGroup(groupId, group)
                _isGroupUpdated.postValue(response.isSuccessful)
                fetchGroups(orderBy = "id")
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            try {
                val response = groupRepository.deleteGroup(groupId)
                if (response.isSuccessful) {
                    fetchGroups(orderBy = "id")
                } else {
                    Log.e("GroupViewModel", "Failed to delete group: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun nextPage() {
        _currentPage++
        fetchGroups(orderBy = "id")
    }

    fun previousPage() {
        if (_currentPage > 1) {
            _currentPage--
            fetchGroups(orderBy = "id")
        }
    }

    fun getDirection(id: Int, callback: (String) -> Unit) {
        directionCallback = callback
        viewModelScope.launch {
            try {
                val response = groupRepository.getDirection(id)
                if (response.isSuccessful) {
                    val directionResponse = response.body()
                    val directionTitle = directionResponse?.title ?: ""
                    callback(directionTitle)
                } else {
                    Log.e("GroupViewModel", "Failed to fetch direction: ${response.errorBody()?.string()}")
                    callback("")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
                callback("")
            }
        }
    }


    fun fetchStudentsForGroup(groupId: Int) {
        viewModelScope.launch {
            try {
                val response = groupRepository.getUsersToGroup(1, 100, orderBy = "user__surname")
                if (response.isSuccessful) {
                    val userToGroupResponse = response.body()
                    if (userToGroupResponse != null) {
                        val allUserToGroup = userToGroupResponse.results
                        if (allUserToGroup != null) {
                            val filteredStudents = allUserToGroup.filter { it.group == groupId }
                            _students.postValue(filteredStudents)
                        } else {
                            Log.e("StudentListViewModel", "Results list is null")
                        }
                    } else {
                        Log.e("StudentListViewModel", "Response body is null")
                    }
                } else {
                    Log.e("StudentListViewModel", "Failed to fetch students: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("StudentListViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun fetchAllStudentsForGroup() {
        viewModelScope.launch {
            try {
                val response = groupRepository.getUsersToGroup(1, 100, orderBy = "user__surname")
                if (response.isSuccessful) {
                    val userToGroupResponse = response.body()
                    if (userToGroupResponse != null) {
                        val allUserToGroup = userToGroupResponse.results
                        if (allUserToGroup != null) {
                            _students.postValue(allUserToGroup)
                        } else {
                            Log.e("StudentListViewModel", "Results list is null")
                        }
                    } else {
                        Log.e("StudentListViewModel", "Response body is null")
                    }
                } else {
                    Log.e("StudentListViewModel", "Failed to fetch students: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("StudentListViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun fetchTeachers() {
        viewModelScope.launch {
            try {
                val response = groupRepository.getTeachers() // Метод репозитория для получения преподавателей
                if (response.isSuccessful) {
                    val teacherResponse = response.body()
                    // Преобразование полученных данных в список преподавателей и обновление LiveData
                    _teachers.postValue(teacherResponse?.results ?: emptyList())
                } else {
                    Log.e("GroupViewModel", "Failed to fetch teachers: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    fun fetchDirections() {
        viewModelScope.launch {
            try {
                val response = groupRepository.getDirections() // Метод репозитория для получения направлений
                if (response.isSuccessful) {
                    val directionResponse = response.body()
                    // Преобразование полученных данных в список направлений и обновление LiveData
                    _directions.postValue(directionResponse?.results ?: emptyList())
                } else {
                    Log.e("GroupViewModel", "Failed to fetch directions: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("GroupViewModel", "Exception occurred: ${e.message}")
            }
        }
    }
}
