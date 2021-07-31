package com.example.barbellweightcalculator.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.barbellweightcalculator.CalculatorViewModel
import com.example.barbellweightcalculator.R
import com.example.barbellweightcalculator.WorkoutViewModel
import com.example.barbellweightcalculator.datatypes.Exercise

//Acts as the fragment that creates the exercise
class ExerciseFragment: Fragment() {

    //reference to CalculatorViewModel
    private val calcModel: CalculatorViewModel by activityViewModels()
    //reference to WorkoutViewModel
    private val workModel: WorkoutViewModel by activityViewModels()

    //ArrayList that has all the workout names
    private val workoutList: ArrayList<String> = arrayListOf()

    private lateinit var exercise: Exercise

    //creates the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)
        workModel.resetModel()

        //Add the names of the workouts from the database into the elements of the spinner

        //add default value
        workoutList.add(resources.getString(R.string.choose_workout))
        //add the names of the workouts to the spinner
        workModel.allWorkouts.observe(viewLifecycleOwner, {workouts ->
            workoutList.clear()
            workoutList.add(resources.getString(R.string.choose_workout))
            for (workout in workouts){
                workoutList.add(workout.workoutName)
            }

        })
        return inflater.inflate(R.layout.fragment_exercise, container, false)

    }

    //creates the listeners used to detect input
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exercise = calcModel.getSelectedExercise()



        view.findViewById<TextView>(R.id.combosForExercise).text = exercise.toString()

        //get access to buttons
        val newWorkBut = view.findViewById<Button>(R.id.createNewWorkoutBut)
        val newExerciseBut = view.findViewById<Button>(R.id.createNewExerciseBut)

        //get access to spinner
        val spinner = view.findViewById<Spinner>(R.id.workoutSpinner)

        val arrayConverter =
            activity?.let { ArrayAdapter(it, R.layout.spinner_text, workoutList) }
        arrayConverter?.setDropDownViewResource(R.layout.spinner_dropdown)

        spinner.adapter = arrayConverter

        //get access to edit text
        val enterName = view.findViewById<EditText>(R.id.enterExerciseName)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                if (parent != null) {
                    //if the spinner is on Choose Workout disable the button
                    if (parent.getItemAtPosition(position).toString() == resources.getString(R.string.choose_workout)){
                        newExerciseBut.isEnabled = false
                    }
                    //if there is a valid workout name selected enable the button
                    else {
                        newExerciseBut.isEnabled = true
                        workModel.setWorkoutName(parent.getItemAtPosition(position).toString())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }

        }


        enterName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //do nothing


                //get the recorded name of the exercise
                if (enterName.text.toString() != "") {
                    workModel.setExerciseName(enterName.text.toString())
                }
                //if there is no name
                else {
                    workModel.setExerciseName("")
                }



            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing


            }
        })

        //navigate to workout creation page if the user wants to create a new workout
        newWorkBut.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseFragment_to_createWorkoutFragment)
        }

        //listener for creating an exercise button
        newExerciseBut.setOnClickListener {
            val entryExercise = exercise
            entryExercise.name = workModel.getExerciseName()
            //if the exercise name is valid
            if (entryExercise.name != ""){
                workModel.addExerciseToWorkout(entryExercise)
                findNavController().popBackStack()
            }
            //if the name is not valid
            else {
                val toast = Toast.makeText(activity, resources.getString(R.string.invalid_exercise_name), Toast.LENGTH_LONG)
                toast.show()
            }
        }



    }
}