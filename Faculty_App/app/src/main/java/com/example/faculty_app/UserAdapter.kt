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
import com.example.faculty_app.data.models.User
import com.example.faculty_app.data.view_models.UserViewModel
import com.example.faculty_app.ui.users.EditUserActivity

class UserAdapter(
    private val viewModel: UserViewModel) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

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

        fun bind(user: User) {
            Log.d("UserAdapter", "Binding user: ${user.name}")
            nameTextView.text = user.name + user.surname

            editButton.setOnClickListener {
                val intent = Intent(itemView.context, EditUserActivity::class.java).apply {
                    putExtra("USER_ID", user.id)
                    putExtra("USER_NAME", user.name)
                    putExtra("USER_EMAIL", user.email)
                    putExtra("USER_LOGIN", user.login)
                    putExtra("USER_SURNAME", user.surname)
                    putExtra("USER_MIDDLE_NAME", user.middle_name)
                    putExtra("USER_BIRTHDAY", user.birthday)
                }
                (itemView.context as Activity).startActivityForResult(intent, 2)
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
