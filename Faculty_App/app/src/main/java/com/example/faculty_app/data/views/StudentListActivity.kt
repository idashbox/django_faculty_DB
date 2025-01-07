package com.example.faculty_app.data.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.ui.adapters.StudentAdapter
import com.example.faculty_app.data.factories.GroupViewModelFactory
import com.example.faculty_app.data.repositories.GroupRepository
import com.example.faculty_app.data.view_models.GroupViewModel

class StudentListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val groupRepository: GroupRepository by lazy { GroupRepository() }
    private val viewModel: GroupViewModel by viewModels {
        GroupViewModelFactory(groupRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val groupId = intent.getIntExtra("GROUP_ID", -1)
        viewModel.fetchStudentsForGroup(groupId)

        viewModel.students.observe(this, Observer { students ->
            adapter = StudentAdapter(students)
            recyclerView.adapter = adapter
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
