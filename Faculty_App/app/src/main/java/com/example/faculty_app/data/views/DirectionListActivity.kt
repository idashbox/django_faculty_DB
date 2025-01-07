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
import com.example.faculty_app.ui.adapters.DirectionAdapter
import com.example.faculty_app.data.factories.DirectionViewModelFactory
import com.example.faculty_app.data.view_models.DirectionViewModel
import com.example.faculty_app.data.repositories.DirectionRepository
import com.example.faculty_app.ui.activities.directions.AddDirectionActivity

class DirectionListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DirectionAdapter
    private val directionRepository: DirectionRepository by lazy {
        DirectionRepository()
    }
    private val directionViewModel: DirectionViewModel by viewModels {
        DirectionViewModelFactory(directionRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direction_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = DirectionAdapter(directionViewModel)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button_add_direction).setOnClickListener {
            startActivityForResult(Intent(this, AddDirectionActivity::class.java), 1)
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        directionViewModel.directions.observe(this, Observer {
            adapter.submitList(it)
        })

        directionViewModel.fetchDirections()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            directionViewModel.fetchDirections()
        }
    }

}
