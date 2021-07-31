package com.example.barbellweightcalculator.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.barbellweightcalculator.R
import com.example.barbellweightcalculator.WorkoutViewModel

//Acts as the fragment that allows the user to create workouts
class CreateWorkoutFragment: Fragment() {

    //reference to the WorkoutViewModel
    private val workModel: WorkoutViewModel by activityViewModels()

    //creates the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)

        //observer used to detect whether the workout was added into the database or not
        workModel.workoutStatus.observe(viewLifecycleOwner, {status ->
            //the workout was successfully added into the database
            when (status) {
                0 -> {
                    findNavController().popBackStack()
                }

                //the workout name is not valid for the database
                2 -> {
                    val toast = Toast.makeText(activity, resources.getString(R.string.invalid_workout_name), Toast.LENGTH_LONG)
                    toast.show()
                }

                //the workout name already exists in the database
                1 -> {
                    val toast = Toast.makeText(activity, resources.getString(R.string.workout_name_taken), Toast.LENGTH_LONG)
                    toast.show()
                }
            }
        })

        return inflater.inflate(R.layout.fragment_createworkout, container, false)

    }


    //creates the listeners used to detect input
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workModel.resetModel()

        //get access to edit text
        val enterName = view.findViewById<EditText>(R.id.enterWorkoutName)

        //get access to button
        val newWorkout = view.findViewById<Button>(R.id.createWorkoutButton)

        enterName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //do nothing


                //get the recorded name of the workout
                if (enterName.text.toString() != "") {
                    workModel.setWorkoutName(enterName.text.toString())
                }
                //if there is no name
                else {
                    workModel.setWorkoutName(null)
                }



            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing


            }
        })

        //create the new workout
        newWorkout.setOnClickListener {

            workModel.createNewWorkout()

        }

    }

    //when the view is reopened or resumed after already being created
    override fun onResume(){
        super.onResume()
        workModel.resetModel()
    }
}