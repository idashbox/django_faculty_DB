package com.example.faculty_app.ui.users

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.UserViewModelFactory
import com.example.faculty_app.data.view_models.UserViewModel
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.UserRepository
import java.text.SimpleDateFormat
import java.util.*

class AddUserActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var roleSpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var userViewModel: UserViewModel

    private var selectedBirthday: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val userRepository = UserRepository()
        val factory = UserViewModelFactory(userRepository)

        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        nameEditText = findViewById(R.id.editTextName)
        surnameEditText = findViewById(R.id.editTextSurname)
        middleNameEditText = findViewById(R.id.editTextMiddleName)
        emailEditText = findViewById(R.id.editTextEmail)
        loginEditText = findViewById(R.id.editTextLogin)
        passwordEditText = findViewById(R.id.editTextPassword)
        birthdayEditText = findViewById(R.id.editTextBirthday)
        genderRadioGroup = findViewById(R.id.radioGroupGender)
        roleSpinner = findViewById(R.id.spinnerRole)
        saveButton = findViewById(R.id.buttonSave)

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
            addUser()
        }

        userViewModel.isUserAdded.observe(this, Observer { isAdded ->
            if (isAdded) {
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Ошибка при добавлении пользователя", Toast.LENGTH_SHORT).show()
            }
        })

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addUser() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val middleName = middleNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val login = loginEditText.text.toString()
        val password = passwordEditText.text.toString()
        val selectedGender = if (genderRadioGroup.checkedRadioButtonId == R.id.radioButtonMale) "Male" else "Female"
        val role = roleSpinner.selectedItem.toString()

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(
            id = 0,
            name = name,
            surname = surname,
            middle_name = middleName,
            email = email,
            login = login,
            password = password,
            birthday = formattedBirthday,
            sex = selectedGender,
            role = role
        )

        userViewModel.addUser(newUser)
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("YYYY-MM-DD", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }
}
