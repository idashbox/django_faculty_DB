package com.example.faculty_app.ui.activities.user_to_group

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

class EditUserToGroupActivity : AppCompatActivity() {

    private lateinit var groupNameEditText: EditText
    private lateinit var userSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var userToGroupViewModel: UserToGroupViewModel

    private var selectedUserId: Int = 0
    private var selectedGroupId: Int = 0
    private var userToGroupId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_to_group)

        val userToGroupRepository = UserToGroupRepository()
        val factory = UserToGroupViewModelFactory(userToGroupRepository)

        userToGroupViewModel =
            ViewModelProvider(this, factory).get(UserToGroupViewModel::class.java)

//        groupNameEditText = findViewById(R.id.editTextGroupName)
//        userSpinner = findViewById(R.id.spinnerUser)
//        saveButton = findViewById(R.id.buttonSave)
//
//        loadUsers()
//        loadGroups()
//
//        saveButton.setOnClickListener {
//            editUserToGroup()
//        }
//
//        userToGroupViewModel.isUserToGroupEdited.observe(this, Observer { isEdited ->
//            if (isEdited) {
//                setResult(RESULT_OK)
//                finish()
//            } else {
//                // Handle failure
//            }
//        })
//
//        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        toolbar.setNavigationOnClickListener {
//            onBackPressed()
//        }
//
//        // Assume userToGroupId is passed in Intent extras
//        userToGroupId = intent.getIntExtra("userToGroupId", 0)
//        loadUserToGroupDetails(userToGroupId)
    }

//    private fun loadUsers() {
//        RetrofitClient.apiService.getUsers().enqueue(object : Callback<List<User>> {
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                if (response.isSuccessful) {
//                    val users = response.body() ?: emptyList()
//                    val userNames = users.map { it.login }
//                    val adapter = ArrayAdapter(this@EditUserToGroupActivity, android.R.layout.simple_spinner_item, userNames)
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
//                    Log.e("EditUserToGroupActivity", "Failed to load users")
//                }
//            }
//
//            override fun onFailure(call: Call<List<User>>, t: Throwable) {
//                Log.e("EditUserToGroupActivity", "Error: ${t.message}")
//            }
//        })
//    }

//    private fun loadGroups() {
//        RetrofitClient.apiService.getGroups().enqueue(object : Callback<List<Group>> {
//            override fun onResponse(call: Call<List<Group>>,

}
