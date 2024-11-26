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
        val teacherName = intent.getStringExtra("TEACHER_NAME")
        val teacherSurname = intent.getStringExtra("TEACHER_SURNAME")
        val teacherMiddleName = intent.getStringExtra("TEACHER_MIDDLE_NAME")
        val teacherEmail = intent.getStringExtra("TEACHER_EMAIL")
        val teacherLogin = intent.getStringExtra("TEACHER_LOGIN")
        val teacherPassword = intent.getStringExtra("TEACHER_PASSWORD")
        val teacherBirthday = intent.getStringExtra("TEACHER_BIRTHDAY")
        val teacherGender = intent.getStringExtra("TEACHER_GENDER") ?: ""
        val teacherYearsOfWork = intent.getStringExtra("TEACHER_YEARS_OF_WORK")
        val departmentId = intent.getIntExtra("TEACHER_DEPARTMENT_ID", 0)

        Log.d("EditTeacherActivity", "Intent extras: TEACHER_ID=$teacherId, TEACHER_NAME=$teacherName, TEACHER_SURNAME=$teacherSurname, TEACHER_MIDDLE_NAME=$teacherMiddleName, TEACHER_EMAIL=$teacherEmail, TEACHER_LOGIN=$teacherLogin, TEACHER_PASSWORD=$teacherPassword, TEACHER_BIRTHDAY=$teacherBirthday, TEACHER_GENDER=$teacherGender, TEACHER_YEARS_OF_WORK=$teacherYearsOfWork, TEACHER_DEPARTMENT_ID=$departmentId")

        // Устанавливаем данные в поля
        nameEditText.setText(teacherName)
        surnameEditText.setText(teacherSurname)
        middleNameEditText.setText(teacherMiddleName)
        emailEditText.setText(teacherEmail)
        loginEditText.setText(teacherLogin)
        passwordEditText.setText(teacherPassword)
        yearsOfWorkEditText.setText(teacherYearsOfWork)

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

        loadDepartments()
        selectedDepartmentId = departmentId

        saveButton.setOnClickListener {
            updateTeacher()
        }

        teacherViewModel.isTeacherUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                Log.d("EditTeacherActivity", "Teacher updated successfully")
                setResult(RESULT_OK)
                finish()
            } else {
                Log.e("EditTeacherActivity", "Error updating teacher")
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
                    val adapter = ArrayAdapter(this@EditTeacherActivity, android.R.layout.simple_spinner_item, departmentTitles)
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
                } else {
                    Log.e("EditTeacherActivity", "Failed to load departments: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Department>>, t: Throwable) {
                Log.e("EditTeacherActivity", "Error loading departments: ${t.message}")
            }
        })
    }

    private fun updateTeacher() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

        val updatedUser = User(
            id = userId,
            name = nameEditText.text.toString(),
            email = emailEditText.text.toString() + "d",
            login = loginEditText.text.toString(),
            password = passwordEditText.text.toString(),
            surname = surnameEditText.text.toString(),
            middle_name = middleNameEditText.text.toString(),
            birthday = formattedBirthday,
            sex = if (genderRadioGroup.checkedRadioButtonId == R.id.radioButtonMale) "Male" else "Female",
            role = "teacher"
        )

        val teacherUpdateRequest = Teacher(
            id = teacherId,
            user = updatedUser,
            department = selectedDepartmentId,
            year_of_start_of_work = yearsOfWorkEditText.text.toString()
        )

        Log.d("EditTeacherActivity", "Updating teacher: $teacherUpdateRequest")

        if (teacherUpdateRequest.year_of_start_of_work.isEmpty()) {
            Log.e("EditTeacherActivity", "Year of work cannot be empty!")
            return
        }

        teacherViewModel.updateTeacher(teacherId, teacherUpdateRequest)

        val updatedUserNew = User(
            id = userId,
            name = nameEditText.text.toString(),
            email = emailEditText.text.toString(),
            login = loginEditText.text.toString(),
            password = passwordEditText.text.toString(),
            surname = surnameEditText.text.toString(),
            middle_name = middleNameEditText.text.toString(),
            birthday = formattedBirthday,
            sex = if (genderRadioGroup.checkedRadioButtonId == R.id.radioButtonMale) "Male" else "Female",
            role = "teacher"
        )

        val teacherUpdateRequestNew = Teacher(
            id = teacherId,
            user = updatedUserNew,
            department = selectedDepartmentId,
            year_of_start_of_work = yearsOfWorkEditText.text.toString()
        )

        Log.d("EditTeacherActivity", "Updating teacher: $teacherUpdateRequest")

        if (teacherUpdateRequest.year_of_start_of_work.isEmpty()) {
            Log.e("EditTeacherActivity", "Year of work cannot be empty!")
            return
        }

        teacherViewModel.updateTeacher(teacherId, teacherUpdateRequestNew)
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }
}

