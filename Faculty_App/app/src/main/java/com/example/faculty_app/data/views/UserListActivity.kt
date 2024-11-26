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
import com.example.faculty_app.UserAdapter
import com.example.faculty_app.data.factories.UserViewModelFactory
import com.example.faculty_app.data.view_models.UserViewModel
import com.example.faculty_app.data.repositories.UserRepository
import com.example.faculty_app.ui.users.AddUserActivity
import com.example.faculty_app.ui.users.EditUserActivity

class UserListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private val userRepository: UserRepository by lazy {
        UserRepository()
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = UserAdapter(
            viewModel = userViewModel,
            onEditClick = { user ->
                val intent = Intent(this, EditUserActivity::class.java).apply {
                    putExtra("USER_ID", user.id)
                    putExtra("USER_NAME", user.name)
                    putExtra("USER_EMAIL", user.email)
                    putExtra("USER_LOGIN", user.login)
                    putExtra("USER_SURNAME", user.surname)
                    putExtra("USER_MIDDLE_NAME", user.middle_name)
                    putExtra("USER_BIRTHDAY", user.birthday)
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button_add_user).setOnClickListener {
            startActivityForResult(Intent(this, AddUserActivity::class.java), 1)
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        userViewModel.users.observe(this, Observer {
            adapter.submitList(it)
        })

        userViewModel.fetchUsers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            userViewModel.fetchUsers()
        }
    }
}
