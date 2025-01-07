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
import com.example.faculty_app.data.view_models.UserToGroupViewModel
import com.example.faculty_app.data.models.UserToGroup
import com.example.faculty_app.ui.activities.user_to_group.EditUserToGroupActivity

class UserToGroupAdapter(private val viewModel: UserToGroupViewModel) : ListAdapter<UserToGroup, UserToGroupAdapter.UserToGroupViewHolder>(
    UserToGroupDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserToGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_to_group_item, parent, false)
        return UserToGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserToGroupViewHolder, position: Int) {
        val userToGroup = getItem(position)
        holder.bind(userToGroup)
    }

    inner class UserToGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.user_to_group_name)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)

        fun bind(userToGroup: UserToGroup) {
            Log.d("TeacherAdapter", "Binding user to group: ${userToGroup.user.name}")
            nameTextView.text = userToGroup.user.name +" " + userToGroup.user.surname

            editButton.setOnClickListener {
                val intent = Intent(itemView.context, EditUserToGroupActivity::class.java).apply {
                    putExtra("TEACHER_ID", userToGroup.id)
                    putExtra("USER_ID", userToGroup.user.id)
                    putExtra("TEACHER_NAME", userToGroup.user.name)
                    putExtra("TEACHER_EMAIL", userToGroup.user.email)
                    putExtra("TEACHER_LOGIN", userToGroup.user.login)
                    putExtra("TEACHER_SURNAME", userToGroup.user.surname)
                    putExtra("TEACHER_MIDDLE_NAME", userToGroup.user.middle_name)
                    putExtra("TEACHER_BIRTHDAY", userToGroup.user.birthday)
                    putExtra("TEACHER_GENDER", userToGroup.user.sex)
                    putExtra("GROUP_ID", userToGroup.group)
                }
                (itemView.context as Activity).startActivityForResult(intent, 2)
            }

            // Обработчик для кнопки удаления
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val userToGroup = getItem(position)
                    AlertDialog.Builder(itemView.context)
                        .setTitle("Подтверждение удаления")
                        .setMessage("Вы уверены, что хотите удалить пользователя ${userToGroup.user.name}?")
                        .setPositiveButton("Да") { _, _ ->
                            viewModel.deleteUserToGroup(userToGroup.id)
                        }
                        .setNegativeButton("Нет", null)
                        .show()
                }
            }
        }
    }

    class UserToGroupDiffCallback : DiffUtil.ItemCallback<UserToGroup>() {
        override fun areItemsTheSame(oldItem: UserToGroup, newItem: UserToGroup): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserToGroup, newItem: UserToGroup): Boolean {
            return oldItem == newItem
        }
    }
}