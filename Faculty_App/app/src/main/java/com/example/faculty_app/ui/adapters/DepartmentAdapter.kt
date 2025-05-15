package com.example.faculty_app.ui.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.view_models.DepartmentViewModel
import com.example.faculty_app.ui.activities.dapartments.EditDepartmentActivity

class DepartmentAdapter(private val viewModel: DepartmentViewModel) : ListAdapter<Department, DepartmentAdapter.DepartmentViewHolder>(
    DepartmentDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        Log.d("DepartmentAdapter", "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.department_item, parent, false)
        return DepartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        Log.d("DepartmentAdapter", "onBindViewHolder called for position: $position")
        val department = getItem(position)
        holder.bind(department)
    }

    inner class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.departmentTitle)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)

        fun bind(department: Department) {
            Log.d("DepartmentAdapter", "Binding department: ${department.title}")
            titleTextView.text = department.title

            // Обработчик для кнопки редактирования
            editButton.setOnClickListener {
                val intent = Intent(itemView.context, EditDepartmentActivity::class.java).apply {
                    putExtra("DEPARTMENT_ID", department.id)
                    putExtra("DEPARTMENT_TITLE", department.title)
                    putExtra("DEPARTMENT_HEAD_ID", department.head_of_department)
                }
                (itemView.context as Activity).startActivityForResult(intent, 2)
            }
        }
    }

    class DepartmentDiffCallback : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem == newItem
        }
    }
}
