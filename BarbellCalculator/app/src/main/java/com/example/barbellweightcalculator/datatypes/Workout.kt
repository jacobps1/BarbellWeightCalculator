package com.example.barbellweightcalculator.datatypes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//represents the table in the SQL database for the workout entry
@Entity(tableName = "workout_table")
data class Workout(@PrimaryKey @ColumnInfo(name ="workoutName") var workoutName: String, @ColumnInfo(name ="exercises") var exercises: MutableList<Exercise>) {
}