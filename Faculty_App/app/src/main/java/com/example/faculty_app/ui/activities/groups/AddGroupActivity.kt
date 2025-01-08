package com.example.faculty_app.ui.activities.groups

import android.os.Bundle
import android.util.Log
import android.widget.*
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
    private lateinit var courseEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var spinnerCurator: Spinner
    private lateinit var spinnerHeadman: Spinner
    private lateinit var spinnerDirection: Spinner

    private var selectedCuratorId: Int = 0
    private var selectedHeadmanId: Int = 0
    private var selectedDirectionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        val groupRepository = GroupRepository()
        val factory = GroupViewModelFactory(groupRepository)
        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)

        groupNameEditText = findViewById(R.id.editNumber)
        courseEditText = findViewById(R.id.editCourse)
        saveButton = findViewById(R.id.buttonSave)
        spinnerCurator = findViewById(R.id.spinnerCurstor)
        spinnerHeadman = findViewById(R.id.spinnerHeadman)
        spinnerDirection = findViewById(R.id.spinnerDirection)

        groupViewModel.fetchTeachers()
        groupViewModel.fetchAllStudentsForGroup()
        groupViewModel.fetchDirections()

        groupViewModel.teachers.observe(this, Observer { curators ->
            val curatorTitles = curators.map { "${it.user.name} ${it.user.surname}" }
            val curatorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, curatorTitles)
            curatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCurator.adapter = curatorAdapter
            spinnerCurator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                    selectedCuratorId = curators[position].id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedCuratorId = 0
                }
            }
        })

        groupViewModel.students.observe(this, Observer { headmen ->
            val headmanTitles = headmen.map { "${it.user.name} ${it.user.surname}" }
            val headmanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, headmanTitles)
            headmanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerHeadman.adapter = headmanAdapter
            spinnerHeadman.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                    selectedHeadmanId = headmen[position].id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedHeadmanId = 0
                }
            }
        })

        groupViewModel.directions.observe(this, Observer { directions ->
            val directionTitles = directions.map { it.title }
            val directionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, directionTitles)
            directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDirection.adapter = directionAdapter
            spinnerDirection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                    selectedDirectionId = directions[position].id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedDirectionId = 0
                }
            }
        })

        saveButton.setOnClickListener {
            addGroup()
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addGroup() {
        val groupName = groupNameEditText.text.toString()
        val course = courseEditText.text.toString()

        if (groupName.isEmpty() || course.isEmpty()) {
            Log.e("AddGroupActivity", "Group name and course must be filled!")
            return
        }

        val newGroup = Group(
            id = 0,
            number = groupName.toInt(),
            course = course.toInt(),
            direction = selectedDirectionId,
            headmen = selectedHeadmanId,
            curator = selectedCuratorId
        )

        groupViewModel.addGroup(newGroup)

        groupViewModel.isGroupAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                setResult(RESULT_OK)
                finish()
            }
        })
    }
}
