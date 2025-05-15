package com.example.faculty_app.ui.activities.groups

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private lateinit var courseEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var groupViewModel: GroupViewModel
    private var groupId: Int = 0
    private lateinit var deleteButton: Button
    private lateinit var spinnerCurator: Spinner
    private lateinit var spinnerHeadman: Spinner
    private lateinit var spinnerDirection: Spinner

    private var selectedCuratorId: Int = 0
    private var selectedHeadmanId: Int = 0
    private var selectedDirectionId: Int = 0
    private lateinit var curatorTitles: List<String>
    private lateinit var headmanTitles: List<String>
    private lateinit var directionTitles: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group)

        val groupRepository = GroupRepository()
        val factory = GroupViewModelFactory(groupRepository)
        groupViewModel = ViewModelProvider(this, factory).get(GroupViewModel::class.java)

        groupNameEditText = findViewById(R.id.editNumber)
        courseEditText = findViewById(R.id.editCourse)
        saveButton = findViewById(R.id.buttonSave)
        spinnerCurator = findViewById(R.id.spinnerCurstor)
        spinnerHeadman = findViewById(R.id.spinnerHeadman)
        spinnerDirection = findViewById(R.id.spinnerDirection)
        deleteButton = findViewById(R.id.button_delete)

        groupId = intent.getIntExtra("GROUP_ID", 0)
        curatorTitles = intent.getStringArrayListExtra("CURATORS") ?: emptyList()
        headmanTitles = intent.getStringArrayListExtra("HEADMEN") ?: emptyList()
        directionTitles = intent.getStringArrayListExtra("DIRECTIONS") ?: emptyList()

        groupViewModel.fetchTeachers()
        groupViewModel.fetchAllStudentsForGroup()
        groupViewModel.fetchDirections()

        setupSpinner(spinnerCurator, curatorTitles)
        setupSpinner(spinnerHeadman, headmanTitles)
        setupSpinner(spinnerDirection, directionTitles)

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

        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить эту группу?")
                .setPositiveButton("Да") { _, _ ->
                    groupViewModel.deleteGroup(groupId)
                    Toast.makeText(this, "Группа удалена", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Нет", null)
                .show()
        }

        saveButton.setOnClickListener {
            editGroup()
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        groupViewModel.groups.observe(this, Observer { groups ->
            val group = groups.find { it.id == groupId }
            group?.let {
                groupNameEditText.setText(it.number.toString())
                courseEditText.setText(it.course.toString())
//                spinnerCurator.setSelection(it.curator - 1)
//                spinnerHeadman.setSelection(it.headmen - 1)
//                spinnerDirection.setSelection(it.direction - 1)
            }
        })
    }

    private fun editGroup() {
        val groupName = groupNameEditText.text.toString()
        val course = courseEditText.text.toString()

        if (groupName.isEmpty() || course.isEmpty()) {
            Log.e("EditGroupActivity", "Group name and course must be filled!")
            return
        }

        val updatedGroup = Group(
            id = groupId,
            number = groupName.toInt(),
            course = course.toInt(),
            direction = selectedDirectionId,
            headmen = selectedHeadmanId,
            curator = selectedCuratorId
        )

        groupViewModel.updateGroup(groupId, updatedGroup)

        groupViewModel.isGroupUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                setResult(RESULT_OK)
                finish()
            }
        })
    }
    private fun setupSpinner(spinner: Spinner, data: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

}
