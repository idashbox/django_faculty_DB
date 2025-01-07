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

class AddDepartmentActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var headIDEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var departmentViewModel: DepartmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_department)

        val departmentRepository = DepartmentRepository()
        val factory = DepartmentViewModelFactory(departmentRepository)

        departmentViewModel = ViewModelProvider(this, factory).get(DepartmentViewModel::class.java)

        titleEditText = findViewById(R.id.editTextTitle)
        headIDEditText = findViewById(R.id.editHead)
        saveButton = findViewById(R.id.buttonSave)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val headId = headIDEditText.text.toString().toInt()

            departmentViewModel.addDepartment(title, headId)
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

