package com.example.faculty_app.ui.teachers

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.TeacherViewModelFactory
import com.example.faculty_app.data.view_models.TeacherViewModel
import com.example.faculty_app.data.network.RetrofitClient
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.TeacherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTeacherActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var departmentSpinner: Spinner
    private lateinit var yearsOfWorkEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var teacherViewModel: TeacherViewModel

    private var selectedDepartmentId: Int = 0
    private var selectedBirthday: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher)

        val teacherRepository = TeacherRepository()
        val factory = TeacherViewModelFactory(teacherRepository)

        teacherViewModel = ViewModelProvider(this, factory).get(TeacherViewModel::class.java)

        nameEditText = findViewById(R.id.editTextName)
        surnameEditText = findViewById(R.id.editTextSurname)
        middleNameEditText = findViewById(R.id.editTextMiddleName)
        emailEditText = findViewById(R.id.editTextEmail)
        loginEditText = findViewById(R.id.editTextLogin)
        passwordEditText = findViewById(R.id.editTextPassword)
        birthdayEditText = findViewById(R.id.editTextBirthday)
        genderRadioGroup = findViewById(R.id.radioGroupGender)
        departmentSpinner = findViewById(R.id.spinnerDepartment)
        yearsOfWorkEditText = findViewById(R.id.editTextYearsOfWork)
        saveButton = findViewById(R.id.buttonSave)

        loadDepartments()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedBirthday.set(Calendar.YEAR, year)
            selectedBirthday.set(Calendar.MONTH, month)
            selectedBirthday.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateBirthdayEditText()
        }

        birthdayEditText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                selectedBirthday.get(Calendar.YEAR),
                selectedBirthday.get(Calendar.MONTH),
                selectedBirthday.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        saveButton.setOnClickListener {
            addTeacher()
        }

        teacherViewModel.isTeacherAdded.observe(this, Observer { isAdded ->
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
                    val adapter = ArrayAdapter(this@AddTeacherActivity, android.R.layout.simple_spinner_item, departmentTitles)
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
                    Log.e("AddTeacherActivity", "Failed to load departments")
                }
            }

            override fun onFailure(call: Call<List<Department>>, t: Throwable) {
                Log.e("AddTeacherActivity", "Error: ${t.message}")
            }
        })
    }

    private fun addTeacher() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val middleName = middleNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val login = loginEditText.text.toString()
        val password = passwordEditText.text.toString()
        val gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }
        val yearsOfWork = yearsOfWorkEditText.text.toString()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || login.isEmpty() || password.isEmpty() || yearsOfWork.isEmpty()) {
            Log.e("AddTeacherActivity", "All fields must be filled!")
            return
        }

        if (selectedDepartmentId == 0) {
            Log.e("AddTeacherActivity", "No department selected!")
            return
        }

        val newTeacher = Teacher(
            id = 0,
            user = User(
                id = 0,
                name = name,
                surname = surname,
                middle_name = middleName,
                email = email,
                login = login,
                password = password,
                birthday = formattedBirthday,
                sex = gender,
                role = "teacher"
            ),
            department = selectedDepartmentId,
            year_of_start_of_work = yearsOfWork
        )

        teacherViewModel.addTeacher(newTeacher)
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("YYYY-MM-DD", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }
}

