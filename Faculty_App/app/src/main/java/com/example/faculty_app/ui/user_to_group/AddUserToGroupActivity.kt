package com.example.faculty_app.ui.user_to_group


import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.UserToGroupViewModelFactory
import com.example.faculty_app.data.view_models.UserToGroupViewModel
import com.example.faculty_app.data.network.RetrofitClient
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.repositories.UserToGroupRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserToGroupActivity : AppCompatActivity() {

    private lateinit var groupNameEditText: EditText
    private lateinit var userSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var userToGroupViewModel: UserToGroupViewModel

    private var selectedUserId: Int = 0
    private var selectedGroupId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_to_group)

        val userToGroupRepository = UserToGroupRepository()
        val factory = UserToGroupViewModelFactory(userToGroupRepository)

        userToGroupViewModel = ViewModelProvider(this, factory).get(UserToGroupViewModel::class.java)

//        groupNameEditText = findViewById(R.id.editTextGroupName)
//        userSpinner = findViewById(R.id.spinnerUser)
        saveButton = findViewById(R.id.buttonSave)

//        loadUsers()
        loadGroups()

        saveButton.setOnClickListener {
            addUserToGroup()
        }

        userToGroupViewModel.isUserToGroupAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                setResult(RESULT_OK)
                finish()
            } else {
                // Handle failure
            }
        })

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

//    private fun loadUsers() {
//        RetrofitClient.apiService.getUsers().enqueue(object : Callback<List<User>> {
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                if (response.isSuccessful) {
//                    val users = response.body() ?: emptyList()
//                    val userNames = users.map { it.login }
//                    val adapter = ArrayAdapter(this@AddUserToGroupActivity, android.R.layout.simple_spinner_item, userNames)
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    userSpinner.adapter = adapter
//
//                    userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
//                            selectedUserId = users[position].id
//                        }
//
//                        override fun onNothingSelected(parentView: AdapterView<*>) {
//                            selectedUserId = 0
//                        }
//                    }
//                } else {
//                    Log.e("AddUserToGroupActivity", "Failed to load users")
//                }
//            }
//
////            override fun onFailure(call: Call<List<User>>, t: Throwable) {
////                Log.e("AddUserToGroupActivity", "Error: ${t.message}")
////            }
//        })
//    }

    private fun loadGroups() {
        RetrofitClient.apiService.getGroups().enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) {
                    val groups = response.body() ?: emptyList()
                    val groupNames = groups.map { it.number.toString() }
                    val adapter = ArrayAdapter(this@AddUserToGroupActivity, android.R.layout.simple_spinner_item, groupNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    userSpinner.adapter = adapter

                    userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                            selectedGroupId = groups[position].id
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            selectedGroupId = 0
                        }
                    }
                } else {
                    Log.e("AddUserToGroupActivity", "Failed to load groups")
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Log.e("AddUserToGroupActivity", "Error: ${t.message}")
            }
        })
    }

    private fun addUserToGroup() {
        if (selectedUserId == 0 || selectedGroupId == 0) {
            Log.e("AddUserToGroupActivity", "User or group not selected!")
            return
        }

//        val newUserToGroup = UserToGroup(
//            id = 0,
//            user = User,
//            groupId = selectedGroupId
//        )

//        userToGroupViewModel.addUserToGroup(newUserToGroup)
    }
}
