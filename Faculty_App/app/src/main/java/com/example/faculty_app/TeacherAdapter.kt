package com.example.faculty_app

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
import com.example.faculty_app.data.view_models.TeacherViewModel
import com.example.faculty_app.data.models.Teacher
import com.example.faculty_app.ui.teachers.EditTeacherActivity

class TeacherAdapter(private val viewModel: TeacherViewModel) : ListAdapter<Teacher, TeacherAdapter.TeacherViewHolder>(TeacherDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.teacher_item, parent, false)
            return TeacherViewHolder(view)
        }

        override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
            val teacher = getItem(position)
            holder.bind(teacher)
        }

        inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.teacherName)
            private val deleteButton: Button = itemView.findViewById(R.id.button_delete)
            private val editButton: Button = itemView.findViewById(R.id.button_edit)

            fun bind(teacher: Teacher) {
                Log.d("TeacherAdapter", "Binding teacher: ${teacher.user.name}")
                nameTextView.text = teacher.user.name + teacher.user.surname

                editButton.setOnClickListener {
                    val intent = Intent(itemView.context, EditTeacherActivity::class.java).apply {
                        putExtra("TEACHER_ID", teacher.id)
                        putExtra("USER_ID", teacher.user.id)
                        putExtra("TEACHER_NAME", teacher.user.name)
                        putExtra("TEACHER_EMAIL", teacher.user.email)
                        putExtra("TEACHER_LOGIN", teacher.user.login)
                        putExtra("TEACHER_SURNAME", teacher.user.surname)
                        putExtra("TEACHER_MIDDLE_NAME", teacher.user.middle_name)
                        putExtra("TEACHER_BIRTHDAY", teacher.user.birthday)
                        putExtra("TEACHER_GENDER", teacher.user.sex)
                        putExtra("TEACHER_YEARS_OF_WORK", teacher.year_of_start_of_work)
                        putExtra("TEACHER_DEPARTMENT_ID", teacher.department)
                    }
                    (itemView.context as Activity).startActivityForResult(intent, 2)
                }

                // Обработчик для кнопки удаления
                deleteButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val teacher = getItem(position)
                        AlertDialog.Builder(itemView.context)
                            .setTitle("Подтверждение удаления")
                            .setMessage("Вы уверены, что хотите удалить пользователя ${teacher.user.name}?")
                            .setPositiveButton("Да") { _, _ ->
                                viewModel.deleteTeacher(teacher.id) // Вызов метода удаления в ViewModel
                            }
                            .setNegativeButton("Нет", null)
                            .show()
                    }
                }
            }
        }

        class TeacherDiffCallback : DiffUtil.ItemCallback<Teacher>() {
            override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
                return oldItem == newItem
            }
        }
    }