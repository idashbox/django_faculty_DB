package com.example.faculty_app.ui.activities.dapartments

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.DepartmentViewModelFactory
import com.example.faculty_app.data.view_models.DepartmentViewModel
import com.example.faculty_app.data.repositories.DepartmentRepository

class EditDepartmentActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var headOfDepartmentID: EditText
    private var departmentId: Int = 0
    private lateinit var departmentViewModel: DepartmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_department)

        val departmentRepository = DepartmentRepository()
        val factory = DepartmentViewModelFactory(departmentRepository)

        departmentViewModel = ViewModelProvider(this, factory).get(DepartmentViewModel::class.java)

        titleEditText = findViewById(R.id.editTextTitle)
        headOfDepartmentID = findViewById(R.id.editHead)
        saveButton = findViewById(R.id.buttonSave)

        departmentId = intent.getIntExtra("DEPARTMENT_ID", 0)
        val headID = intent.getIntExtra("DEPARTMENT_HEAD_ID", 0)
        val departmentTitle = intent.getStringExtra("DEPARTMENT_TITLE")
        headOfDepartmentID.setText(headID.toString())
        titleEditText.setText(departmentTitle)

        saveButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val headId = headOfDepartmentID.text.toString().toInt()

            departmentViewModel.updateDepartment(departmentId, updatedTitle, headId)
        }

        departmentViewModel.isDepartmentUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                setResult(RESULT_OK)
                finish()
            } else {
                // Покажите сообщение об ошибке, если необходимо
            }
        })


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
