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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.faculty_app.R
import com.example.faculty_app.data.models.Group
import com.example.faculty_app.data.view_models.GroupViewModel
import com.example.faculty_app.data.views.StudentListActivity
import com.example.faculty_app.ui.activities.groups.EditGroupActivity

class GroupAdapter(private val viewModel: GroupViewModel) : ListAdapter<Group, GroupAdapter.GroupViewHolder>(
    GroupDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = getItem(position)
        holder.bind(group)
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.group_name)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)
        private val viewStudentsButton: Button = itemView.findViewById(R.id.button_view_students)

        fun bind(group: Group) {
            viewModel.getDirection(group.direction) { direction ->
                if (direction.isNotEmpty()) {
                    nameTextView.text = "$direction курс: ${group.course} группа: ${group.number}"
                } else {
                    nameTextView.text = "Group Name курс: ${group.course} группа: ${group.number}"
                }
            }

            editButton.setOnClickListener {
                val intent = Intent(itemView.context, EditGroupActivity::class.java).apply {
                    putExtra("GROUP_ID", group.id)
                    putExtra("GROUP_NUMBER", group.number)
                    putExtra("GROUP_COURSE", group.course)
                    putExtra("GROUP_DIRECTION", group.direction)
                    putExtra("GROUP_HEADMAN", group.headmen)
                    putExtra("GROUP_CURATOR", group.curator)
                }
                (itemView.context as Activity).startActivityForResult(intent, 2)
            }

            viewStudentsButton.setOnClickListener {
                val intent = Intent(itemView.context, StudentListActivity::class.java).apply {
                    putExtra("GROUP_ID", group.id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }
}
