package com.example.microsoft.room.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserData(var username: String,
               var groupId:Int,
               var dob:String,
                var password:String,
               @PrimaryKey(autoGenerate = true)  val id: Int = 0)