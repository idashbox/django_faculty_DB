package com.example.faculty_app.data.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.DepartmentAdapter
import com.example.faculty_app.R
import com.example.faculty_app.data.view_models.DepartmentViewModel
import com.example.faculty_app.data.factories.DepartmentViewModelFactory
import com.example.faculty_app.data.repositories.DepartmentRepository
import com.example.faculty_app.ui.dapartments.AddDepartmentActivity

class DepartmentListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DepartmentAdapter
    private val departmentRepository: DepartmentRepository by lazy {
        DepartmentRepository()
    }
    private val departmentViewModel: DepartmentViewModel by viewModels {
        DepartmentViewModelFactory(departmentRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = DepartmentAdapter(departmentViewModel)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button_add_department).setOnClickListener {
            startActivityForResult(Intent(this, AddDepartmentActivity::class.java), 1)
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        departmentViewModel.departments.observe(this, Observer {
            Log.d("DepartmentListActivity", "Departments updated: $it")
            adapter.submitList(it)
        })

        departmentViewModel.fetchDepartments()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            departmentViewModel.fetchDepartments()
        }
    }
}
