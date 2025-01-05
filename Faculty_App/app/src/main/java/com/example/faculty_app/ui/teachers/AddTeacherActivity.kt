package com.example.faculty_app.ui.teachers

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.TeacherViewModelFactory
import com.example.faculty_app.data.view_models.TeacherViewModel
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.TeacherRepository
import java.text.SimpleDateFormat
import java.util.*

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

        teacherViewModel.fetchDepartments()

        teacherViewModel.departments.observe(this, Observer { departments ->
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

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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
            validateAndAddTeacher()
        }
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }

    private fun validateAndAddTeacher() {
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val middleName = middleNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val login = loginEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val yearsOfWork = yearsOfWorkEditText.text.toString().trim()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || login.isEmpty() || password.isEmpty() || yearsOfWork.isEmpty()) {
            showToast("All fields must be filled!")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email address!")
            return
        }

        val yearsOfWorkInt = yearsOfWork.toIntOrNull()
        if (yearsOfWorkInt == null || yearsOfWorkInt <= 0) {
            showToast("Years of work must be a positive number!")
            return
        }

        if (selectedDepartmentId == 0) {
            showToast("Please select a department!")
            return
        }

        val gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }

        if (gender.isEmpty()) {
            showToast("Please select a sex!")
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

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

        teacherViewModel.isTeacherAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                showToast("Teacher added successfully!")
                setResult(RESULT_OK)
                finish()
            } else {
                showToast("Failed to add teacher. Please try again.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
