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
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.view_models.UserViewModel

class UserAdapter(
    private val viewModel: UserViewModel,
    private val onEditClick: (User) -> Unit // Коллбэк для обработки клика на редактирование
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.userName)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)

        init {
            // Обработчик для кнопки редактирования
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = getItem(position)
                    onEditClick(user) // Вызываем переданный коллбэк
                }
            }

            // Обработчик для кнопки удаления
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = getItem(position)
                    AlertDialog.Builder(itemView.context)
                        .setTitle("Подтверждение удаления")
                        .setMessage("Вы уверены, что хотите удалить пользователя ${user.name}?")
                        .setPositiveButton("Да") { _, _ ->
                            viewModel.deleteUser(user.id) // Вызов метода удаления в ViewModel
                        }
                        .setNegativeButton("Нет", null)
                        .show()
                }
            }
        }

        fun bind(user: User) {
            nameTextView.text = user.name // Устанавливаем имя пользователя
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
