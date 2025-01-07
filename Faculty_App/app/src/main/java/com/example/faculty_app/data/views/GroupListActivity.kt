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
import com.example.faculty_app.ui.adapters.GroupAdapter
import com.example.faculty_app.data.factories.GroupViewModelFactory
import com.example.faculty_app.data.view_models.GroupViewModel
import com.example.faculty_app.data.repositories.GroupRepository
import com.example.faculty_app.ui.activities.groups.AddGroupActivity

class GroupListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter
    private val groupRepository: GroupRepository by lazy {
        GroupRepository()
    }
    private val groupViewModel: GroupViewModel by viewModels {
        GroupViewModelFactory(groupRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = GroupAdapter(groupViewModel)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button_add_group).setOnClickListener {
            startActivityForResult(Intent(this, AddGroupActivity::class.java), 1)
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        groupViewModel.groups.observe(this, Observer {
            adapter.submitList(it)
        })

        groupViewModel.fetchGroups()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            groupViewModel.fetchGroups()
        }
    }

}
