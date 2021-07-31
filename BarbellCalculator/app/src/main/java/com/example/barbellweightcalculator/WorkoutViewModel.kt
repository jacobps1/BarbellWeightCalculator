package com.example.barbellweightcalculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.barbellweightcalculator.database.WorkoutEntryRepository
import com.example.barbellweightcalculator.database.WorkoutRoomDatabase
import com.example.barbellweightcalculator.datatypes.Exercise
import com.example.barbellweightcalculator.datatypes.Workout
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

//ViewModel that contains the repository for the workouts and exercises
class WorkoutViewModel(application: Application): AndroidViewModel(application)  {

    //sets up the coroutines
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+ Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    //represents the workouts in the database
    val allWorkouts: LiveData<List<Workout>>

    //represents the repository that accesses and modifies the contents of the database
    private val repository: WorkoutEntryRepository = WorkoutEntryRepository(WorkoutRoomDatabase.getDatabase(application).workoutDao())

    //the name that the user has selected for the workout they are creating
    private var workoutName: String? = null

    //indicates whether the workout exists or not in the database
    private var workoutExists: Boolean = true


    //indicates the status of the workout getting added into the table
    private var _workoutStatus = MutableLiveData<Int>()
    val workoutStatus: LiveData<Int>
        get() = _workoutStatus

    //indicates the current selected workout
    private lateinit var currentWorkout: Workout

    //represents the name of the exercise that the user is creating
    private var exerciseName: String = ""


    //intializes the values of the workout status and the workout list
    init {
        allWorkouts = repository.allWorkouts
        _workoutStatus.value = -1
    }


    //cancels the coroutines
    override fun onCleared(){
        super.onCleared()
        parentJob.cancel()
    }

    //resets the model to initial values
    fun resetModel(){
        _workoutStatus.value = -1
        exerciseName = ""
        workoutName = null
    }
    //set the current workout that is selected
    fun setCurrentWorkout(workout: Workout){
        currentWorkout = workout
    }
    //get the name of the current workout
    fun getCurrentWorkoutName():String {
        return  currentWorkout.workoutName
    }
    //get the current workout
    fun getCurrentWorkout(): Workout{
        return currentWorkout
    }

    //set the new exercise's name
    fun setExerciseName(name: String){
        exerciseName = name
    }
    //get the new exercise's name
    fun getExerciseName(): String {
        return exerciseName
    }

    //set the workout name for the new workout that will be created/have the exercise added to
    fun setWorkoutName(name: String?){
        workoutName = name
    }

    //add an exercise into the workout
    fun addExerciseToWorkout(exercise: Exercise) {
        scope.launch(Dispatchers.IO) {

            var temp: Workout? = workoutName?.let { repository.getWorkout(it) }
            temp?.exercises?.add(exercise)
            createWorkout(temp)
        }
    }



    //deletes the given workout
    fun removeWorkout(workout: Workout){
        deleteWorkout(workout)
    }

    //deletes all workouts in the database
    fun removeAllWorkouts(){
        deleteAllWorkouts()
    }

    //remove the given exercise from the workout
    fun deleteExerciseFromWorkout(position: Int){
        scope.launch(Dispatchers.IO) {

            val temp: Workout? = repository.getWorkout(currentWorkout.workoutName)
            temp?.exercises?.removeAt(position)
            if (temp != null) {
                //update the current workout
                currentWorkout = temp
            }
            createWorkout(temp)
        }
    }

    //remove all exercises in the workout
    fun deleteAllExercisesFromWorkout(){
        scope.launch(Dispatchers.IO) {
            val temp: Workout? = repository.getWorkout(currentWorkout.workoutName)
            temp?.exercises?.clear()
            if (temp != null) {
                //update the current workout
                currentWorkout = temp
            }
            createWorkout(temp)
        }
    }

    //create a new workout and add it into the database
    // 0 means the workout was successfully added to database
    // 1 means workout wasn't added because the name already exists
    // 2 means workout wasn't added because the name is invalid
    fun createNewWorkout(){

            scope.launch(Dispatchers.IO) {

                //check to see if the name is not null
                if (workoutName != null){
                    val job =  doesWorkoutExistHelper(workoutName!!)
                    workoutExists = job

                    //check to see if workout name exists already
                    if (!workoutExists){
                        val temp = createWorkoutObject()
                        createWorkout(temp)
                        //indicate the workout was successfully added to the database
                        _workoutStatus.postValue(0)
                    }
                    else {
                        //indicate the workout already exists in the database
                        _workoutStatus.postValue(1)
                    }


                }
                //indicate the name is null
                else {
                    _workoutStatus.postValue(2)
                }

            }

    }
    //indicates whether the workout currently exists in the database or not
    private fun doesWorkoutExistHelper(workoutName: String): Boolean{

        val temp: Workout? = repository.getWorkout(workoutName)

        return temp != null
    }

    //insert the workout into the database
    private fun createWorkout(workout: Workout?) = scope.launch(Dispatchers.IO){
        if (workout != null) {
            repository.insert(workout)
        }
    }

    //delete all of the workouts in the database
    private fun deleteAllWorkouts() = scope.launch(Dispatchers.IO){
        repository.deleteAll()
    }
    //delete the given workout in the database
    private fun deleteWorkout(workout: Workout) = scope.launch(Dispatchers.IO){
        repository.delete(workout)
    }

    //create a Workout Object
    private fun createWorkoutObject(): Workout? {
        val exerciseList: MutableList<Exercise> = arrayListOf()

        return workoutName?.let { Workout(it, exerciseList) }
    }
}