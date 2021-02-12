package com.example.microsoft.room
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.microsoft.room.entites.EventData
import com.example.microsoft.room.entites.LocationData
import com.example.microsoft.room.entites.TaskData
import com.example.microsoft.room.entites.UserData


@Database(
    entities = [UserData:: class,EventData:: class, TaskData:: class, LocationData::class],
    version = 1,
    exportSchema = false
)

abstract class database: RoomDatabase() {

    abstract fun getDao(): roomInterface

    companion object{
        @Volatile  private var instance: database? = null
        private val Lock = Any()

        operator fun invoke(context: Context)= instance
            ?: synchronized(Lock){
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            database::class.java, "todo-list.db")
            .build()
    }

}