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
import com.example.faculty_app.ui.adapters.UserToGroupAdapter
import com.example.faculty_app.data.factories.UserToGroupViewModelFactory
import com.example.faculty_app.data.view_models.UserToGroupViewModel
import com.example.faculty_app.data.repositories.UserToGroupRepository
import com.example.faculty_app.ui.activities.user_to_group.AddUserToGroupActivity

class UserToGroupListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserToGroupAdapter
    private val userToGroupRepository: UserToGroupRepository by lazy {
        UserToGroupRepository()
    }
    private val userToGroupViewModel: UserToGroupViewModel by viewModels {
        UserToGroupViewModelFactory(userToGroupRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_to_group_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = UserToGroupAdapter(userToGroupViewModel)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button_add_user_to_group).setOnClickListener {
            startActivityForResult(Intent(this, AddUserToGroupActivity::class.java), 1)
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        userToGroupViewModel.userToGroups.observe(this, Observer {
            adapter.submitList(it)
        })

        userToGroupViewModel.fetchUserToGroups()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            userToGroupViewModel.fetchUserToGroups()
        }
    }

}
