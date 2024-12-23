package com.example.faculty_app.ui.directions

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.DirectionViewModelFactory
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.repositories.DirectionRepository
import com.example.faculty_app.data.view_models.DirectionViewModel

class EditDirectionActivity : AppCompatActivity() {

    private lateinit var titleEdit: EditText
    private lateinit var codeEdit: EditText
    private lateinit var degreeEdit: EditText
    private lateinit var departmentSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var directionViewModel: DirectionViewModel
    private var selectedDepartmentId: Int = 0
    private var directionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_direction)

        val directionRepository = DirectionRepository()
        val factory = DirectionViewModelFactory(directionRepository)

        directionViewModel = ViewModelProvider(this, factory).get(DirectionViewModel::class.java)

        titleEdit = findViewById(R.id.editTitle)
        codeEdit = findViewById(R.id.editCode)
        degreeEdit = findViewById(R.id.editDegree)
        departmentSpinner = findViewById(R.id.spinnerDepartment)
        saveButton = findViewById(R.id.buttonSave)

        directionId = intent.getIntExtra("DIRECTION_ID", 0)
        val title = intent.getStringExtra("DIRECTION_TITLE")
        val code = intent.getIntExtra("DIRECTION_CODE", 0)
        val degree = intent.getStringExtra("DIRECTION_DEGREE")
        val departmentId = intent.getIntExtra("DIRECTION_DEPARTMENT", 0)

        titleEdit.setText(title)
        codeEdit.setText(code.toString())
        degreeEdit.setText(degree)

        directionViewModel.fetchDepartments()

        directionViewModel.departments.observe(this, Observer { departments ->
            val departmentTitles = departments.map { it.title }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentTitles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            departmentSpinner.adapter = adapter

            departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                    selectedDepartmentId = departments[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedDepartmentId = 0
                }
            }
        })

        selectedDepartmentId = departmentId

        saveButton.setOnClickListener {
            updateTeacher()
        }

        directionViewModel.isDirectionUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                Log.d("EditDirectionActivity", "Direction updated successfully")
                setResult(RESULT_OK)
                finish()
            } else {
                Log.e("EditDirectionActivity", "Error updating direction")
                // Handle failure
            }
        })

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun updateTeacher() {
        val directionUpdateRequest = Direction(
            id = directionId,
            title = titleEdit.text.toString(),
            code = codeEdit.text.toString(),
            degree = degreeEdit.text.toString(),
            department = selectedDepartmentId,
        )

        Log.d("EditTeacherActivity", "Updating direction: $directionUpdateRequest")

        directionViewModel.updateDirection(directionId, directionUpdateRequest)
    }
}
