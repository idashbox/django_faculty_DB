package com.example.faculty_app.data.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.TeacherAdapter
import com.example.faculty_app.data.factories.TeacherViewModelFactory
import com.example.faculty_app.data.view_models.TeacherViewModel
import com.example.faculty_app.data.repositories.TeacherRepository
import com.example.faculty_app.ui.teachers.AddTeacherActivity
import com.example.faculty_app.ui.teachers.EditTeacherActivity

class TeacherListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeacherAdapter
    private val teacherRepository: TeacherRepository by lazy {
        TeacherRepository()
    }
    private val teacherViewModel: TeacherViewModel by viewModels {
        TeacherViewModelFactory(teacherRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TeacherAdapter(
            viewModel = teacherViewModel,
            onEditClick = { teacher ->
                val intent = Intent(this, EditTeacherActivity::class.java).apply {
                    putExtra("TEACHER_ID", teacher.id)
                    putExtra("USER_ID", teacher.user.id)
                    putExtra("TEACHER_NAME", teacher.user.name)
                    putExtra("TEACHER_EMAIL", teacher.user.email)
                    putExtra("TEACHER_LOGIN", teacher.user.login)
                    putExtra("TEACHER_SURNAME", teacher.user.surname)
                    putExtra("TEACHER_MIDDLE_NAME", teacher.user.middle_name)
                    putExtra("TEACHER_BIRTHDAY", teacher.user.birthday)
                    putExtra("TEACHER_GENDER", teacher.user.sex)
                    putExtra("TEACHER_YEARS_OF_WORK", teacher.year_of_start_of_work)
                    putExtra("TEACHER_DEPARTMENT_ID", teacher.department)
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button_add_teacher).setOnClickListener {
            startActivityForResult(Intent(this, AddTeacherActivity::class.java), 1)
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        teacherViewModel.teachers.observe(this, Observer {
            adapter.submitList(it)
        })

        teacherViewModel.fetchTeachers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            teacherViewModel.fetchTeachers()
        }
    }
}
