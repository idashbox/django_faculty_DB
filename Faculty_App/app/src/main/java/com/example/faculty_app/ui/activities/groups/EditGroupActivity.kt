package com.example.faculty_app.ui.activities.groups

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

class EditGroupActivity : AppCompatActivity() {

    private lateinit var groupNameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var groupViewModel: GroupViewModel
    private var groupId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group)

        val groupRepository = GroupRepository()
        val factory = GroupViewModelFactory(groupRepository)

        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)

//        groupNameEditText = findViewById(R.id.editTextGroupName)
        saveButton = findViewById(R.id.buttonSave)

        saveButton.setOnClickListener {
            editGroup()
        }

//        groupViewModel.isGroupEdited.observe(this, Observer { isEdited ->
//            if (isEdited) {
//                setResult(RESULT_OK)
//                finish()
//            } else {
//                // Handle failure
//            }
//        })

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        groupId = intent.getIntExtra("groupId", 0)
//        loadGroupDetails(groupId)
    }

//    private fun loadGroupDetails(id: Int) {
//        groupViewModel.getGroup(id).observe(this, Observer { group ->
//            group?.let {
//                groupNameEditText.setText(it.number)
//            }
//        })
//    }

    private fun editGroup() {
        val groupName = groupNameEditText.text.toString()

        if (groupName.isEmpty()) {
            Log.e("EditGroupActivity", "Group name must be filled!")
            return
        }

//        val updatedGroup = Group(
//            id = groupId,
//            number = groupName
//        )

//        groupViewModel.editGroup(updatedGroup)
    }
}
