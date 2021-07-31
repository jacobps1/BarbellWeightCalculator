package com.example.barbellweightcalculator.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barbellweightcalculator.R
import com.example.barbellweightcalculator.WorkoutViewModel
import com.example.barbellweightcalculator.datatypes.Exercise

//Acts as the fragment that displays the exercises of a given workout
class WorkoutDetailFragment: Fragment() {

    //reference to WorkoutViewModel
    private val model: WorkoutViewModel by activityViewModels()
    //reference to custom adapter for RecyclerView
    private val adapter = ExerciseListAdapter()

    //creates the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)

        //observer that sets the list of exercises into the adapter
        model.allWorkouts.observe(viewLifecycleOwner, {
            adapter.setExercises(model.getCurrentWorkout().exercises)

        })

        return inflater.inflate(R.layout.fragment_workoutdetail, container, false)

    }


    //creates the listener for the delete all button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setting up reference to the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.exerciseRecycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        view.findViewById<TextView>(R.id.exercisesInLabel).text = resources.getString(R.string.exercises_in, model.getCurrentWorkoutName())

        //listener for delete all button. Queues a warning message before allowing user to delete
        view.findViewById<Button>(R.id.deleteAllExercisesButton).setOnClickListener {

            //make sure there are still exercises available to delete
            if (adapter.itemCount != 0) {
                val builder1 = AlertDialog.Builder(context)
                builder1.setMessage(resources.getString(R.string.delete_every_exercise))
                builder1.setCancelable(false)

                builder1.setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    model.deleteAllExercisesFromWorkout()

                }

                builder1.setNegativeButton(
                    "No"
                ) { dialog, _ ->

                    dialog.cancel()


                }

                val alert = builder1.create()
                alert.setTitle(resources.getString(R.string.warning))
                alert.show()
            }

        }
    }

    //inner class that represents the adapter for the RecyclerView
    inner class ExerciseListAdapter :
        RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>(){

        //represents the list of exercises
        private var exercises = emptyList<Exercise>()

        //sets the list of exercises displayed in the RecyclerView
        internal fun setExercises(exercises: List<Exercise>) {
            this.exercises = exercises

            notifyDataSetChanged()
        }

        //gets the number of items in the adapter
        override fun getItemCount(): Int {

            return exercises.size
        }


        //creates the ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {


            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_weightcombination, parent, false)
            return ExerciseViewHolder(v)
        }

        //assigns the views within the ViewHolder values
        override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {

            //check to see if the bar is included in the combination calculation
            val includeStatus = if (exercises[position].barIncluded){
                resources.getString(R.string.include_bar, exercises[position].bar)
            } else {
                resources.getString(R.string.not_include)
            }

            holder.view.findViewById<TextView>(R.id.workoutName).text = resources.getString(R.string.exercise_name_and_weight, exercises[position].name ,exercises[position].weight)
            holder.view.findViewById<TextView>(R.id.numberExercisesLabel).text = includeStatus

            holder.view.findViewById<TextView>(R.id.weightCombo).text = exercises[position].toString()

            holder.view.findViewById<Button>(R.id.viewExerciseButton).text = resources.getString(R.string.delete_exercise)

            //set the listener for the delete exercise button. Queues a warning message before the user deletes the exercise
            holder.view.findViewById<Button>(R.id.viewExerciseButton).setOnClickListener {

                val builder1 = AlertDialog.Builder(context)
                builder1.setMessage(resources.getString(R.string.delete_current_exercise_message, exercises[position].name))
                builder1.setCancelable(false)

                builder1.setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    model.deleteExerciseFromWorkout(position)

                }

                builder1.setNegativeButton(
                    "No"
                ) { dialog, _ ->

                    dialog.cancel()


                }

                val alert = builder1.create()
                alert.setTitle(resources.getString(R.string.warning))
                alert.show()


            }
        }

        //custom view holder that represents the view of an individual item in the RecyclerView
        inner class ExerciseViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){




            }


        }
    }
}