package com.example.barbellweightcalculator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.barbellweightcalculator.datatypes.Workout


@Database(entities = [Workout::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)

//represents the creation of the database
abstract class WorkoutRoomDatabase: RoomDatabase(){
    abstract fun workoutDao(): WorkoutEntryDao


    companion object {
        @Volatile
        private var INSTANCE: WorkoutRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): WorkoutRoomDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutRoomDatabase::class.java,
                    "WorkoutRoomDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}