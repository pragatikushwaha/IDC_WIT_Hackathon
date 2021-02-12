package com.example.microsoft.room.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EventData(var createdBy: String,
                var groupId:Int,
                var eventName:String,
                var eventDate:String,
                var eventTime:String,
                var eventInfo:String,
                @PrimaryKey(autoGenerate = true)  val id: Int = 0)