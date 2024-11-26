package com.example.faculty_app

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

class TeacherAdapter(private val viewModel: TeacherViewModel,
    private val onEditClick: (Teacher) -> Unit
    ) : ListAdapter<Teacher, TeacherAdapter.TeacherViewHolder>(TeacherDiffCallback()) {

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

            init {
                // Обработчик для кнопки редактирования
                editButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val teacher = getItem(position)
                        onEditClick(teacher) // Вызываем переданный коллбэк
                    }
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

            fun bind(teacher: Teacher) {
                nameTextView.text = teacher.user.name + " " + teacher.user.surname // Устанавливаем имя пользователя
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