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
}
