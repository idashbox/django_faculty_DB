package com.example.faculty_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.UserAdapter
import com.example.faculty_app.data.network.RetrofitClient
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.views.DepartmentListActivity
import com.example.faculty_app.data.views.TeacherListActivity
import com.example.faculty_app.data.views.UserListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_users).setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }

        findViewById<Button>(R.id.button_teachers).setOnClickListener {
            startActivity(Intent(this, TeacherListActivity::class.java))
        }

        findViewById<Button>(R.id.button_departments).setOnClickListener {
            startActivity(Intent(this, DepartmentListActivity::class.java))
        }
    }

    private fun fetchUsers() {
        RetrofitClient.apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        adapter.submitList(users)
                        Log.d("MainActivity", "Users: $users")
                    }
                } else {
                    // Логирование кода ответа и тела ответа
                    Log.e("MainActivity", "Response failed: ${response.code()} ${response.message()}")
                    response.errorBody()?.let {
                        Log.e("MainActivity", "Error body: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
}
