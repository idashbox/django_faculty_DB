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
import com.example.faculty_app.data.view_models.DirectionViewModel
import com.example.faculty_app.data.models.Direction
import com.example.faculty_app.ui.activities.directions.EditDirectionActivity

class DirectionAdapter(private val viewModel: DirectionViewModel) : ListAdapter<Direction, DirectionAdapter.DirectionViewHolder>(
    DirectionDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.direction_item, parent, false)
        return DirectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DirectionViewHolder, position: Int) {
        val group = getItem(position)
        holder.bind(group)
    }

    inner class DirectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.direction_name)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)
        private val editButton: Button = itemView.findViewById(R.id.button_edit)

        fun bind(direction: Direction) {
            Log.d("DirectionAdapter", "Binding user to group: ${direction.title}")
            nameTextView.text = direction.title

            editButton.setOnClickListener {
                val intent = Intent(itemView.context, EditDirectionActivity::class.java).apply {
                    putExtra("DIRECTION_ID", direction.id)
                    putExtra("DIRECTION_TITLE", direction.title)
                    putExtra("DIRECTION_CODE", direction.code)
                    putExtra("DIRECTION_DEGREE", direction.degree)
                    putExtra("DIRECTION_DEPARTMENT", direction.department)
                }
                (itemView.context as Activity).startActivityForResult(intent, 2)
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val group = getItem(position)
                    AlertDialog.Builder(itemView.context)
                        .setTitle("Подтверждение удаления")
                        .setMessage("Вы уверены, что хотите удалить направление ${direction.title}?")
                        .setPositiveButton("Да") { _, _ ->
                            viewModel.deleteDirection(group.id)
                        }
                        .setNegativeButton("Нет", null)
                        .show()
                }
            }
        }
    }

    class DirectionDiffCallback : DiffUtil.ItemCallback<Direction>() {
        override fun areItemsTheSame(oldItem: Direction, newItem: Direction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Direction, newItem: Direction): Boolean {
            return oldItem == newItem
        }
    }
}