package com.example.barbellweightcalculator.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.barbellweightcalculator.datatypes.Workout

//this class represents the repository that conducts the database queries and obtains the values
class WorkoutEntryRepository(private val workoutDao: WorkoutEntryDao) {

    //represents the list of all workouts
    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAllWorkouts()

    //conducts the search query for the given workout
    @WorkerThread
    fun getWorkout(workoutName: String): Workout{
        return workoutDao.getWorkout(workoutName)
    }

    //conducts the insert query for the given workout
    @WorkerThread
    fun insert(workout: Workout){
        workoutDao.insertWorkout(workout)
    }

    //conducts the delete query for the database
    @WorkerThread
    fun deleteAll(){
        workoutDao.DeleteAll()
    }

    //conducts the delete query for the given workout
    @WorkerThread
    fun delete(entry: Workout){
        workoutDao.deleteWatchlistEntry(entry.workoutName)
    }
}

