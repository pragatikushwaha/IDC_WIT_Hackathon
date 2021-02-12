package com.example.microsoft.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.microsoft.room.entites.EventData
import com.example.microsoft.room.entites.LocationData
import com.example.microsoft.room.entites.TaskData
import com.example.microsoft.room.entites.UserData

@Dao
interface roomInterface {
    @Insert
    suspend fun addUserData(data:UserData)

    @Query("Select * from UserData")
    suspend fun getUserData(): List<UserData>

    @Insert
    suspend fun addEventData(data:EventData)

    @Query("Select * from EventData")
    fun getEventData(): LiveData<List<EventData>>

    @Insert
    suspend fun addTaskData(data:TaskData)

    @Query("Select * from TaskData where createdBy = :user")
    fun getTaskDataGiven(user:String): LiveData<List<TaskData>>


    @Query("Select * from TaskData where assignedTo = :user")
    fun getTaskDataAssigned(user:String): LiveData<List<TaskData>>


    @Query("Select * from UserData where groupId = :groupID")
    fun getUserDataByGroupId(groupID:Int):List<UserData>

    @Query("Select * from LocationData where  groupId = :groupID and username!= :Username")
    fun getLocationData(groupID: Int,Username:String): LiveData<List<LocationData>>

//    @Query("UPDATE LocationData SET lastTime = :lastTime, lat = :lat,long=:log,cityName= :city,subadminArea=:subadminArea,adminArea=:adminArea,countryName=:country  WHERE username =:username")
//    fun updateLocation(username:String,lat:Double,log: Double,lastTime:String,city:String,subadminArea:String,adminArea:String,country:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLocationData(data:LocationData)
}
