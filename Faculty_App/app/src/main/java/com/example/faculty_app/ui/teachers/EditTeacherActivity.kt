package com.example.faculty_app.ui.teachers

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.TeacherViewModelFactory
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.TeacherRepository
import com.example.faculty_app.data.view_models.TeacherViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditTeacherActivity : AppCompatActivity() {

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
    private var selectedDepartmentId: Int = 0
    private var selectedBirthday: Calendar = Calendar.getInstance()
    private var teacherId: Int = 0
    private var userId: Int = 0
    private lateinit var teacherViewModel: TeacherViewModel
    private val TAG = "EditTeacherActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_teacher)

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

        teacherId = intent.getIntExtra("TEACHER_ID", 0)
        userId = intent.getIntExtra("USER_ID", 0)
        nameEditText.setText(intent.getStringExtra("TEACHER_NAME"))
        surnameEditText.setText(intent.getStringExtra("TEACHER_SURNAME"))
        middleNameEditText.setText(intent.getStringExtra("TEACHER_MIDDLE_NAME"))
        emailEditText.setText(intent.getStringExtra("TEACHER_EMAIL"))
        loginEditText.setText(intent.getStringExtra("TEACHER_LOGIN"))
        passwordEditText.setText(intent.getStringExtra("TEACHER_PASSWORD"))
        yearsOfWorkEditText.setText(intent.getStringExtra("TEACHER_YEARS_OF_WORK"))
        val teacherBirthday = intent.getStringExtra("TEACHER_BIRTHDAY")
        val teacherGender = intent.getStringExtra("TEACHER_GENDER") ?: ""
        selectedDepartmentId = intent.getIntExtra("TEACHER_DEPARTMENT_ID", 0)

        if (teacherGender == "Female") {
            genderRadioGroup.check(R.id.radioButtonFemale)
        } else if (teacherGender == "Male") {
            genderRadioGroup.check(R.id.radioButtonMale)
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

        if (!teacherBirthday.isNullOrEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedBirthday.time = dateFormat.parse(teacherBirthday) ?: Date()
            updateBirthdayEditText()
        }

        teacherViewModel.fetchDepartments()
        teacherViewModel.departments.observe(this, Observer { departments ->
            val departmentTitles = departments.map { it.title }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentTitles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            departmentSpinner.adapter = adapter

            departmentSpinner.setSelection(departments.indexOfFirst { it.id == selectedDepartmentId })
            departmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                    selectedDepartmentId = departments[position].id
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    selectedDepartmentId = 0
                }
            }
        })

        saveButton.setOnClickListener {
            updateTeacher()
        }

        teacherViewModel.isTeacherUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                Toast.makeText(this, "Teacher updated successfully", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Error updating teacher", Toast.LENGTH_SHORT).show()
            }
        })

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }

    private fun updateTeacher() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val middleName = middleNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim() + "d"
        val login = loginEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val yearsOfWork = yearsOfWorkEditText.text.toString().trim()
        
        Log.d(TAG, "UpdateTeacher: name=$name, surname=$surname, email=$email, department=$selectedDepartmentId")

        if (name.isEmpty() || surname.isEmpty() || middleName.isEmpty() || email.isEmpty() || login.isEmpty() || password.isEmpty() || yearsOfWork.isEmpty()) {
            Log.e(TAG, "All fields must be filled!") // Логируем ошибку
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e(TAG, "Invalid email address: $email") // Логируем ошибку
            Toast.makeText(this, "Invalid email address!", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDepartmentId == 0) {
            Log.e(TAG, "Department not selected") // Логируем ошибку
            Toast.makeText(this, "Please select a department!", Toast.LENGTH_SHORT).show()
            return
        }

        val gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }

        if (gender.isEmpty()) {
            Log.e(TAG, "Gender not selected") // Логируем ошибку
            Toast.makeText(this, "Please select a gender!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = User(
            id = userId,
            name = name,
            email = email,
            login = login,
            password = password,
            surname = surname,
            middle_name = middleName,
            birthday = formattedBirthday,
            sex = gender,
            role = "teacher"
        )

        val teacherUpdateRequest = Teacher(
            id = teacherId,
            user = updatedUser,
            department = selectedDepartmentId,
            year_of_start_of_work = yearsOfWork
        )

        Log.d(TAG, "Sending update request for teacher ID: $teacherId") // Логируем отправку запроса

        teacherViewModel.updateTeacher(teacherId, teacherUpdateRequest)

        // Логирование в случае успешного или неуспешного обновления
        teacherViewModel.isTeacherUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                Log.d(TAG, "Teacher updated successfully") // Логируем успешное обновление
                Toast.makeText(this, "Teacher updated successfully", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Log.e(TAG, "Error updating teacher") // Логируем ошибку
                Toast.makeText(this, "Error updating teacher", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
