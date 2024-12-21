package com.example.faculty_app.ui.directions

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
import com.example.faculty_app.data.factories.DirectionViewModelFactory
import com.example.faculty_app.data.view_models.DirectionViewModel
import com.example.faculty_app.data.network.RetrofitClient
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.data.repositories.DirectionRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AddDirectionActivity : AppCompatActivity() {

    private lateinit var titleEdit: EditText
    private lateinit var codeEdit: EditText
    private lateinit var degreeEdit: EditText
    private lateinit var departmentSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var directionViewModel: DirectionViewModel

    private var selectedDepartmentId: Int = 0
    private var selectedBirthday: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_direction)

        val directionRepository = DirectionRepository()
        val factory = DirectionViewModelFactory(directionRepository)

        directionViewModel = ViewModelProvider(this, factory).get(DirectionViewModel::class.java)

        titleEdit = findViewById(R.id.editTitle)
        codeEdit = findViewById(R.id.editCode)
        degreeEdit = findViewById(R.id.editDegree)
        departmentSpinner = findViewById(R.id.spinnerDepartment)
        saveButton = findViewById(R.id.buttonSave)

        loadDepartments()

        saveButton.setOnClickListener {
            addDirection()
        }

        directionViewModel.isDirectionAdded.observe(this, Observer { isAdded ->
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

    private fun loadDepartments() {
        RetrofitClient.apiService.getDepartments().enqueue(object : Callback<List<Department>> {
            override fun onResponse(call: Call<List<Department>>, response: Response<List<Department>>) {
                if (response.isSuccessful) {
                    val departments = response.body() ?: emptyList()
                    val departmentTitles = departments.map { it.title }
                    val adapter = ArrayAdapter(this@AddDirectionActivity, android.R.layout.simple_spinner_item, departmentTitles)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    departmentSpinner.adapter = adapter

                    departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                            selectedDepartmentId = departments[position].id
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            selectedDepartmentId = 0
                        }
                    }
                } else {
                    Log.e("AddDirectionActivity", "Failed to load directions")
                }
            }

            override fun onFailure(call: Call<List<Department>>, t: Throwable) {
                Log.e("AddDirectionActivity", "Error: ${t.message}")
            }
        })
    }

    private fun addDirection() {

        val title = titleEdit.text.toString()
        val code = codeEdit.text
        val degree = degreeEdit.text.toString()

        if (title.isEmpty() || degree.isEmpty()) {
            Log.e("AddDirectionActivity", "All fields must be filled!")
            return
        }

        if (selectedDepartmentId == 0) {
            Log.e("AddDirectionActivity", "No department selected!")
            return
        }

        val newDirection = Direction(
            id = 0,
            title = title,
            code = 0,
            degree = degree,
            department = selectedDepartmentId,
        )

        directionViewModel.addDirection(newDirection)
    }
}

