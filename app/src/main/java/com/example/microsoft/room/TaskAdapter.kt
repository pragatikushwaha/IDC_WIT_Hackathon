package com.example.microsoft.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.microsoft.R
import com.example.microsoft.room.entites.TaskData
import kotlinx.android.synthetic.main.task_item_row.view.*

class TaskAdapter(private val taskData: List<TaskData>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun getItemCount() = taskData.size

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
        holder.view.task_name.text =taskData[position].taskName
        holder.view.task_date.text =taskData[position].taskDate
        holder.view.task_time.text =taskData[position].taskTime
        holder.view.created_By.text =taskData[position].createdBy
        holder.view.assigned_To.text =taskData[position].assignedTo
        holder.view.task_info.text =taskData[position].taskInfo

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item_row, parent, false)
        return ViewHolder(itemView)
    }

    //1
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        var view: View = v

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            //Log.d("RecyclerView", "CLICK!")
        }


    }

}
