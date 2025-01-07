package com.example.faculty_app.data.views

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.ui.adapters.TeacherAdapter
import com.example.faculty_app.data.factories.TeacherViewModelFactory
import com.example.faculty_app.data.view_models.TeacherViewModel
import com.example.faculty_app.data.repositories.TeacherRepository
import com.example.faculty_app.ui.activities.teachers.AddTeacherActivity
import com.example.faculty_app.ui.fragments.filter.FilterBottomSheet

class TeacherListActivity : AppCompatActivity(), FilterBottomSheet.OnFilterApplyListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeacherAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var spinnerFilter: Spinner
    private lateinit var buttonPreviousPage: Button
    private lateinit var buttonNextPage: Button
    private lateinit var buttonSearch: Button
    private lateinit var buttonFilter: Button
    private var isFilterApplied = false

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
        buttonFilter = findViewById(R.id.buttonFilter)

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

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selectedFilter = filterOptions[position]
                when (selectedFilter) {
                    "Имя" -> teacherViewModel.fetchTeachers(ordering = "user__name")
                    "Фамилия" -> teacherViewModel.fetchTeachers(ordering = "user__surname")
                    "Отчество" -> teacherViewModel.fetchTeachers(ordering = "user__middle_name")
                    "Дата рождения" -> teacherViewModel.fetchTeachers(ordering = "user__birthday")
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

        buttonSearch.setOnClickListener {
            val searchQuery = editTextSearch.text.toString().trim()
            teacherViewModel.searchTeachers(searchQuery)
        }

        buttonFilter.setOnClickListener {
            if (isFilterApplied) {
                isFilterApplied = false
                buttonFilter.text = "Фильтр"
                teacherViewModel.fetchTeachers()
            } else {
                val bottomSheet = FilterBottomSheet(this)
                bottomSheet.show(supportFragmentManager, FilterBottomSheet.TAG)
            }
        }
    }

    override fun onFilterApplied(
        minAge: Int?,
        maxAge: Int?,
        minYearStart: Int?,
        maxYearStart: Int?,
        sex: String?
    ) {
        isFilterApplied = true
        buttonFilter.text = "Х Убрать Фильтр"

        teacherViewModel.fetchTeachers(
            minAge = minAge,
            maxAge = maxAge,
            minYearStart = minYearStart,
            maxYearStart = maxYearStart,
            sex = sex
        )
    }
}
