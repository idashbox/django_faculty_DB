package com.example.faculty_app.data.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
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

class TeacherListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeacherAdapter
    private lateinit var editTextSearch: EditText
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

        teacherViewModel.teachers.observe(this, Observer {
            adapter.submitList(it)
        })

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selectedFilter = filterOptions[position]
                val orderBy = when (selectedFilter) {
                    "Имя" -> "user__name"
                    "Фамилия" -> "user__surname"
                    "Отчество" -> "user__middle_name"
                    "Дата рождения" -> "user__birthday"
                    else -> null
                }
                teacherViewModel.fetchTeachers(ordering = orderBy)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        buttonPreviousPage.setOnClickListener {
            teacherViewModel.previousPage()
        }

        buttonNextPage.setOnClickListener {
            teacherViewModel.nextPage()
        }

        buttonSearch.setOnClickListener {
            val searchQuery = editTextSearch.text.toString().trim()
            teacherViewModel.fetchTeachers(name = searchQuery)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val minAge = data.getIntExtra("minAge", -1).takeIf { it != -1 }
            val maxAge = data.getIntExtra("maxAge", -1).takeIf { it != -1 }
            val minYearStart = data.getIntExtra("minYearStart", -1).takeIf { it != -1 }
            val maxYearStart = data.getIntExtra("maxYearStart", -1).takeIf { it != -1 }
            val sex = data.getStringExtra("sex")

            teacherViewModel.fetchTeachers(
                sex = sex,
                yearOfStartOfWork = minYearStart?.toString() ?: maxYearStart?.toString()
            )
        }
    }

    companion object {
        private const val FILTER_REQUEST_CODE = 100
    }
}
