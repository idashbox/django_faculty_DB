package com.example.faculty_app.data.views

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private lateinit var spinnerFilter: Spinner
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
        spinnerFilter = findViewById(R.id.spinnerFilter)
        buttonPreviousPage = findViewById(R.id.buttonPreviousPage)
        buttonNextPage = findViewById(R.id.buttonNextPage)
        buttonSearch = findViewById(R.id.buttonSearch)

        val filterOptions = arrayOf("Имя", "Фамилия", "Отчество", "Дата рождения")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = filterAdapter

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

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selectedFilter = filterOptions[position]
                when (selectedFilter) {
                    "Имя" -> teacherViewModel.fetchTeachers(orderBy = "user__name")
                    "Фамилия" -> teacherViewModel.fetchTeachers(orderBy = "user__surname")
                    "Отчество" -> teacherViewModel.fetchTeachers(orderBy = "user__middle_name")
                    "Дата рождения" -> teacherViewModel.fetchTeachers(orderBy = "user__birthday")
                }
                teacherViewModel.fetchTeachers()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        buttonPreviousPage.setOnClickListener {
            teacherViewModel.previousPage()
        }

        buttonNextPage.setOnClickListener {
            teacherViewModel.nextPage()
        }

        // Обработчик нажатия на кнопку поиска
        buttonSearch.setOnClickListener {
            val searchQuery = editTextSearch.text.toString().trim()
            teacherViewModel.searchTeachers(searchQuery)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            teacherViewModel.fetchTeachers()
        }
    }
}
