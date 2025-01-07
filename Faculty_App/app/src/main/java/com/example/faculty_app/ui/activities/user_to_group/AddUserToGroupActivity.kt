package com.example.faculty_app.ui.activities.user_to_group


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.UserToGroupViewModelFactory
import com.example.faculty_app.data.view_models.UserToGroupViewModel
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.data.repositories.UserToGroupRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddUserToGroupActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var groupSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var userToGroupViewModel: UserToGroupViewModel

    private var selectedBirthday: Calendar = Calendar.getInstance()

    private var selectedGroupId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_to_group)

        val userToGroupRepository = UserToGroupRepository()
        val factory = UserToGroupViewModelFactory(userToGroupRepository)

        userToGroupViewModel = ViewModelProvider(this, factory).get(UserToGroupViewModel::class.java)

        nameEditText = findViewById(R.id.editTextName)
        surnameEditText = findViewById(R.id.editTextSurname)
        middleNameEditText = findViewById(R.id.editTextMiddleName)
        emailEditText = findViewById(R.id.editTextEmail)
        loginEditText = findViewById(R.id.editTextLogin)
        passwordEditText = findViewById(R.id.editTextPassword)
        birthdayEditText = findViewById(R.id.editTextBirthday)
        genderRadioGroup = findViewById(R.id.radioGroupGender)
        groupSpinner = findViewById(R.id.spinnerGroup)
        saveButton = findViewById(R.id.buttonSave)

        userToGroupViewModel.fetchGroups()

        userToGroupViewModel.groups.observe(this, Observer { groups ->
            val departmentTitles = groups.map { it.number + it.course }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentTitles)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            groupSpinner.adapter = adapter

            groupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                    selectedGroupId = groups[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedGroupId = 0
                }
            }
        })

        saveButton.setOnClickListener {
            validateAndAddUserToGroup()
        }

        userToGroupViewModel.isUserToGroupAdded.observe(this, Observer { isAdded ->
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
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }

    private fun validateAndAddUserToGroup() {
        val name = nameEditText.text.toString().trim()
        val surname = surnameEditText.text.toString().trim()
        val middleName = middleNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val login = loginEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || login.isEmpty() || password.isEmpty()) {
            showToast("All fields must be filled!")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email address!")
            return
        }

        val gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }

        if (gender.isEmpty()) {
            showToast("Please select a gender!")
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

        val newUserToGroup = UserToGroup(
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
                role = "user_to_group"
            ),
            group = selectedGroupId
        )

        userToGroupViewModel.addUserToGroup(newUserToGroup)

        userToGroupViewModel.isUserToGroupAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                showToast("Student added successfully!")
                setResult(RESULT_OK)
                finish()
            } else {
                showToast("Failed to add student. Please try again.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
