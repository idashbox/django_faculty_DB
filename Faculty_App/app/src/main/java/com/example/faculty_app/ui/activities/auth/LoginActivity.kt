package com.example.faculty_app.ui.activities.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.faculty_app.R
import com.example.faculty_app.data.models.AuthToken
import com.example.faculty_app.data.network.RetrofitClient
import com.example.faculty_app.data.repositories.AuthRepository
import com.example.faculty_app.ui.activities.MainActivity
import com.example.faculty_app.data.views.TeacherListActivity
import com.example.faculty_app.data.views.UserToGroupListActivity
import com.example.faculty_app.ui.activities.MainActivityForTeacher
import com.example.faculty_app.ui.activities.MainActivityForUserToGroup
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(applicationContext)
        authRepository = AuthRepository()
        setContentView(R.layout.activity_login)

        val usernameField = findViewById<EditText>(R.id.editTextUsername)
        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = authRepository.login(username, password)

                if (response.isSuccessful) {
                    val authToken = response.body()
                    if (authToken != null) {
                        saveAuthData(authToken)

                        val userProfileResponse = RetrofitClient.apiService.getUserProfile()

                        if (userProfileResponse.isSuccessful) {
                            val user = userProfileResponse.body()
                            if (user != null) {
                                val role = user.role

                                when (role) {
                                    "teacher" -> startActivity(Intent(this@LoginActivity, MainActivityForTeacher::class.java))
                                    "usertogroup" -> startActivity(Intent(this@LoginActivity, MainActivityForUserToGroup::class.java))
                                    else -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                }
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Не удалось получить данные пользователя", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Ошибка при получении профиля: ${userProfileResponse.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Ошибка авторизации: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private suspend fun saveAuthData(authToken: AuthToken) {
        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)

        prefs.edit()
            .putString("token", authToken.token)
            .apply()

        val token = prefs.getString("token", null)

        if (token != null) {
            val userProfileResponse = RetrofitClient.apiService.getUserProfile()
            Log.d("Token Check", "Token: |$token|")
        } else {
            Toast.makeText(this, "Токен не найден, авторизация требуется", Toast.LENGTH_SHORT).show()
        }
    }

}
