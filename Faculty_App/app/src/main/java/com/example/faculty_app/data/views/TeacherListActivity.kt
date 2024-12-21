package com.example.faculty_app.data.views

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var editTextSearch: EditText
    private lateinit var spinnerDepartmentFilter: Spinner
    private lateinit var buttonPreviousPage: Button
    private lateinit var buttonNextPage: Button
    private lateinit var buttonSearch: Button
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

        adapter = TeacherAdapter(teacherViewModel)
        recyclerView.adapter = adapter

        editTextSearch = findViewById(R.id.editTextSearch)
        spinnerDepartmentFilter = findViewById(R.id.spinnerDepartmentFilter)
        buttonPreviousPage = findViewById(R.id.buttonPreviousPage)
        buttonNextPage = findViewById(R.id.buttonNextPage)
        buttonSearch = findViewById(R.id.buttonSearch)

        val buttonAddTeacher: Button = findViewById(R.id.button_add_teacher)
        buttonAddTeacher.setOnClickListener {
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

        teacherViewModel.statistics.observe(this, Observer { statistics ->
            // Обработка статистики
        })

        buttonSearch.setOnClickListener {
            val searchQuery = editTextSearch.text.toString()
            teacherViewModel.fetchTeachers(nameFilter = searchQuery)
        }

        spinnerDepartmentFilter.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selectedDepartmentId = parentView.getItemAtPosition(position) as? Int
                teacherViewModel.fetchTeachers(departmentFilter = selectedDepartmentId)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        })

        buttonPreviousPage.setOnClickListener {
            teacherViewModel.previousPage()
        }

        buttonNextPage.setOnClickListener {
            teacherViewModel.nextPage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            teacherViewModel.fetchTeachers()
        }
    }
}
