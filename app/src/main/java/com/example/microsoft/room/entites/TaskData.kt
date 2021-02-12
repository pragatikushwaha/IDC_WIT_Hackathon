package com.example.microsoft.room.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TaskData(var createdBy: String,
                var assignedTo:String,
                var groupId:Int,
                var taskName:String,
                var taskDate:String,
                var taskTime:String,
                var taskInfo:String,
                @PrimaryKey(autoGenerate = true)  val id: Int = 0)