package com.example.faculty_app.ui.activities.users

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.faculty_app.R
import com.example.faculty_app.data.factories.UserViewModelFactory
import com.example.faculty_app.data.view_models.UserViewModel
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.repositories.UserRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditUserActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var deleteButton: Button
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var birthdayEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private var selectedBirthday: Calendar = Calendar.getInstance()
    private lateinit var saveButton: Button
    private var userId: Int = 0
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

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
        saveButton = findViewById(R.id.buttonSave)
        deleteButton = findViewById(R.id.button_delete)

        userId = intent.getIntExtra("USER_ID", 0)
        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userLogin = intent.getStringExtra("USER_LOGIN")
        val userPassword = intent.getStringExtra("USER_PASSWORD")
        val userSurname = intent.getStringExtra("USER_SURNAME")
        val userMiddleName = intent.getStringExtra("USER_MIDDLE_NAME")
        val userBirthday = intent.getStringExtra("USER_BIRTHDAY")
        val userGender = intent.getStringExtra("USER_GENDER") ?: ""

        Log.d(
            "EditUserActivity",
            "Intent extras: USER_ID=$userId, USER_NAME=$userName, USER_EMAIL=$userEmail, USER_LOGIN=$userLogin, USER_PASSWORD=$userPassword, USER_SURNAME=$userSurname, USER_MIDDLE_NAME=$userMiddleName, USER_BIRTHDAY=$userBirthday, USER_GENDER=$userGender"
        )

        nameEditText.setText(userName)
        surnameEditText.setText(userSurname)
        middleNameEditText.setText(userMiddleName)
        emailEditText.setText(userEmail)
        loginEditText.setText(userLogin)
        passwordEditText.setText(userPassword)

        if (userGender == "Female") {
            genderRadioGroup.check(R.id.radioButtonFemale)
        } else if (userGender == "Male") {
            genderRadioGroup.check(R.id.radioButtonMale)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedBirthday.set(Calendar.YEAR, year)
            selectedBirthday.set(Calendar.MONTH, month)
            selectedBirthday.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateBirthdayEditText()
        }

        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить пользователя ${nameEditText.text}?")
                .setPositiveButton("Да") { _, _ ->
                    userViewModel.deleteUser(userId)
                    Toast.makeText(this, "Преподаватель удалён", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Нет", null)
                .show()
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
            updateUser()
        }

        userViewModel.isUserUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated) {
                Log.d("EditUserActivity", "User updated successfully")
                setResult(RESULT_OK)
                finish()
            } else {
                Log.e("EditUserActivity", "Error updating user")
                Toast.makeText(this, "Ошибка при обновлении пользователя", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun updateUser() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedBirthday = dateFormat.format(selectedBirthday.time)

        val updatedUser = User(
            id = userId,
            name = nameEditText.text.toString(),
            email = emailEditText.text.toString(),
            login = loginEditText.text.toString(),
            password = passwordEditText.text.toString(),
            surname = surnameEditText.text.toString(),
            middle_name = middleNameEditText.text.toString(),
            birthday = formattedBirthday,
            sex = if (genderRadioGroup.checkedRadioButtonId == R.id.radioButtonMale) "Male" else "Female",
            role = ""
        )

        Log.d("EditUserActivity", "Updating user: $updatedUser")

        userViewModel.updateUser(userId, updatedUser)
    }

    private fun updateBirthdayEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        birthdayEditText.setText(dateFormat.format(selectedBirthday.time))
    }
}
