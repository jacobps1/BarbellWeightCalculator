package com.example.barbellweightcalculator.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.barbellweightcalculator.datatypes.*

//this interface represents the data access object used for queries for the Workout entry in the SQL database
@Dao
interface WorkoutEntryDao {

    //gets all the workouts from the database
    @Query("SELECT * FROM workout_table")
    fun getAllWorkouts(): LiveData<List<Workout>>

    //gets the provided workout
    @Query("SELECT * FROM workout_table WHERE workoutName = :entry")
    fun getWorkout(entry: String): Workout

    //deletes the provided workout from the table
    @Query("DELETE FROM workout_table WHERE workoutName = :entry")
    fun deleteWatchlistEntry(entry: String)

    //deletes every entry from the table
    @Query("DELETE FROM workout_table")
    fun DeleteAll()

    //inserts the given workout into the list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkout(movie: Workout)


}