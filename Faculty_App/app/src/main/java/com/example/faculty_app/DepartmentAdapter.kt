package com.example.faculty_app

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
import com.example.faculty_app.data.models.Department
import com.example.faculty_app.data.view_models.DepartmentViewModel
import com.example.faculty_app.ui.dapartments.EditDepartmentActivity

class DepartmentAdapter(private val viewModel: DepartmentViewModel) : ListAdapter<Department, DepartmentAdapter.DepartmentViewHolder>(DepartmentDiffCallback()) {

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
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        fun bind(department: Department) {
            Log.d("DepartmentAdapter", "Binding department: ${department.title}")
            titleTextView.text = department.title

            // Обработчик для кнопки редактирования
            editButton.setOnClickListener {
                Log.d("DepartmentAdapter", "Edit button clicked for department: ${department.title}")
                val intent = Intent(itemView.context, EditDepartmentActivity::class.java).apply {
                    putExtra("DEPARTMENT_ID", department.id)
                    putExtra("DEPARTMENT_TITLE", department.title)
                    putExtra("DEPARTMENT_HEAD_ID", department.head_of_department)
                }
                itemView.context.startActivity(intent)
            }

            // Обработчик для кнопки удаления
            deleteButton.setOnClickListener {
                Log.d("DepartmentAdapter", "Delete button clicked for department: ${department.title}")
                AlertDialog.Builder(itemView.context)
                    .setTitle("Подтверждение удаления")
                    .setMessage("Вы уверены, что хотите удалить кафедру ${department.title}?")
                    .setPositiveButton("Да") { _, _ ->
                        Log.d("DepartmentAdapter", "Deleting department with id: ${department.id}")
                        viewModel.deleteDepartment(department.id) // Удаление через ViewModel
                    }
                    .setNegativeButton("Нет", null)
                    .show()
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
