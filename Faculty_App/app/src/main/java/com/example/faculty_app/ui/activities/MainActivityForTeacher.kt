package com.example.faculty_app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.ui.adapters.UserAdapter
import com.example.faculty_app.data.views.DepartmentListActivity
import com.example.faculty_app.data.views.DirectionListActivity
import com.example.faculty_app.data.views.GroupListActivity
import com.example.faculty_app.data.views.TeacherListActivity
import com.example.faculty_app.data.views.UserListActivity
import com.example.faculty_app.data.views.UserToGroupListActivity
import com.example.faculty_app.ui.activities.auth.LoginActivity

class MainActivityForTeacher : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_for_teacher)

        findViewById<Button>(R.id.button_teachers).setOnClickListener {
            startActivity(Intent(this, TeacherListActivity::class.java))
        }
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            logoutUser()
        }

        findViewById<Button>(R.id.button_departments).setOnClickListener {
            startActivity(Intent(this, DepartmentListActivity::class.java))
        }
        findViewById<Button>(R.id.button_user_to_group).setOnClickListener {
            startActivity(Intent(this, UserToGroupListActivity::class.java))
        }
        findViewById<Button>(R.id.button_groups).setOnClickListener {
            startActivity(Intent(this, GroupListActivity::class.java))
        }
        findViewById<Button>(R.id.button_directions).setOnClickListener {
            startActivity(Intent(this, DirectionListActivity::class.java))
        }
    }
    private fun logoutUser() {
        val prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
