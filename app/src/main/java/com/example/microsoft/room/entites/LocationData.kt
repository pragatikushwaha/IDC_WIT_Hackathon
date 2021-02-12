package com.example.microsoft.room.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationData(@PrimaryKey var username: String,
               var groupId:Int,
               var lat:Double,
               var longi:Double,
               var lastTime:String,
               var cityName:String,
               var subAdminArea:String,
               var adminArea:String,
               var countryName:String)