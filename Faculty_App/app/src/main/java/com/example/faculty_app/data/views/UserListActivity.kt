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
import com.example.faculty_app.ui.adapters.UserAdapter
import com.example.faculty_app.data.factories.UserViewModelFactory
import com.example.faculty_app.data.repositories.UserRepository
import com.example.faculty_app.data.view_models.UserViewModel
import com.example.faculty_app.ui.fragments.filter.FilterBottomSheet
import com.example.faculty_app.ui.activities.users.AddUserActivity

class UserListActivity : AppCompatActivity(), FilterBottomSheet.OnFilterApplyListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var spinnerFilter: Spinner
    private lateinit var buttonPreviousPage: Button
    private lateinit var buttonNextPage: Button
    private lateinit var buttonSearch: Button
    private lateinit var buttonFilter: Button
    private var isFilterApplied = false

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

        adapter = UserAdapter(userViewModel)
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

        val buttonAddUser: Button = findViewById(R.id.button_add_user)
        buttonAddUser.setOnClickListener {
            startActivityForResult(Intent(this, AddUserActivity::class.java), 1)
        }

        userViewModel.users.observe(this, Observer {
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
                    "Имя" -> userViewModel.fetchUsers(ordering = "user__name")
                    "Фамилия" -> userViewModel.fetchUsers(ordering = "user__surname")
                    "Отчество" -> userViewModel.fetchUsers(ordering = "user__middle_name")
                    "Дата рождения" -> userViewModel.fetchUsers(ordering = "user__birthday")
                }
                userViewModel.fetchUsers()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        buttonPreviousPage.setOnClickListener {
            userViewModel.previousPage()
        }

        buttonNextPage.setOnClickListener {
            userViewModel.nextPage()
        }

        buttonSearch.setOnClickListener {
            val searchQuery = editTextSearch.text.toString().trim()
            userViewModel.searchUsers(searchQuery)
        }

        buttonFilter.setOnClickListener {
            if (isFilterApplied) {
                isFilterApplied = false
                buttonFilter.text = "Фильтр"
                userViewModel.fetchUsers()
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

        userViewModel.fetchUsers(
            minAge = minAge,
            maxAge = maxAge,
            sex = sex
        )
    }
}
