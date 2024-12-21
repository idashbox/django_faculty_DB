package com.example.faculty_app.ui.groups

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.GroupViewModelFactory
import com.example.faculty_app.data.view_models.GroupViewModel
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.repositories.GroupRepository

class AddGroupActivity : AppCompatActivity() {

    private lateinit var groupNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val groupRepository = GroupRepository()
        val factory = GroupViewModelFactory(groupRepository)

        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)

//        groupNameEditText = findViewById(R.id.editTextGroupName)
        saveButton = findViewById(R.id.buttonSave)

        saveButton.setOnClickListener {
            addGroup()
        }

        groupViewModel.isGroupAdded.observe(this, Observer { isAdded ->
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

    private fun addGroup() {
        val groupName = groupNameEditText.text.toString()

        if (groupName.isEmpty()) {
            Log.e("AddGroupActivity", "Group name must be filled!")
            return
        }

//        val newGroup = Group(
//            id = 0,
//            number = groupName
//        )

//        groupViewModel.addGroup(newGroup)
    }
}
