package com.siroca.todo09.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siroca.todo09.ItemListener
import com.siroca.todo09.room.TaskModel
import com.siroca.todo09.databinding.ItemTaskBinding

class TaskAdapter(private val list: List<TaskModel>, private val listener: ItemListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(taskModel: TaskModel) {
            binding.taskTv.text = taskModel.task
            binding.dateTv.text = taskModel.date
            binding.regularTv.text = taskModel.regular
            itemView.setOnClickListener {
                listener.itemUpdate(taskModel)
            }
        }
    }
}