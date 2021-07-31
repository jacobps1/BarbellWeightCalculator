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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barbellweightcalculator.R
import com.example.barbellweightcalculator.WorkoutViewModel
import com.example.barbellweightcalculator.datatypes.Workout


//Acts as the fragment that displays the list of workouts
class WorkoutFragment: Fragment() {

    //reference to custom adapter for RecyclerView
    private val adapter = WorkoutListAdapter()

    //reference to WorkoutViewModel
    private val model: WorkoutViewModel by activityViewModels()

    //creates the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)

        //observer that sets the list of workouts into the adapter
        model.allWorkouts.observe(viewLifecycleOwner, {workouts ->
            adapter.setWorkouts(workouts)
        })
        return inflater.inflate(R.layout.fragment_workout, container, false)

    }

    //creates the listener for the delete all button and the create workout button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setting up reference to the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.workoutRecycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        //get references to the buttons
        val deleteAll = view.findViewById<Button>(R.id.deleteAllButton)

        val addNewWork = view.findViewById<Button>(R.id.createNewWorkoutButton)


        //listener for create workout button. Navigates to create workout fragment if button is clicked
        addNewWork.setOnClickListener {
            findNavController().navigate(R.id.action_workoutFragment_to_createWorkoutFragment)
        }

        //listener for delete all button. Queues a warning message before allowing user to delete
        deleteAll.setOnClickListener {

            val builder1 = AlertDialog.Builder(context)
            builder1.setMessage(resources.getString(R.string.delete_every_workout))
            builder1.setCancelable(false)

            builder1.setPositiveButton(
                "Yes"
            ) { _, _ ->
                model.removeAllWorkouts()

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
    //inner class that represents the adapter for the RecyclerView
    inner class WorkoutListAdapter :
        RecyclerView.Adapter<WorkoutListAdapter.WorkoutViewHolder>(){

        //represents the list of workouts
        private var workouts = emptyList<Workout>()

        //sets the list of workouts displayed in the RecyclerView
        internal fun setWorkouts(workouts: List<Workout>) {
            
            this.workouts = workouts

            notifyDataSetChanged()
        }

        //gets the number of items in the adapter
        override fun getItemCount(): Int {

            return workouts.size
        }

        //creates the ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {


            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_workouts, parent, false)
            return WorkoutViewHolder(v)
        }

        //assigns the views within the ViewHolder values
        override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {




            holder.view.findViewById<TextView>(R.id.workoutName).text = workouts[position].workoutName
            holder.view.findViewById<TextView>(R.id.numberExercisesLabel).text = resources.getString(R.string.number_exercises, workouts[position].exercises.size)


            //set the listener for the view exercises button. Navigates to list of exercises fragment
            holder.view.findViewById<Button>(R.id.viewExerciseButton).setOnClickListener {

                model.setCurrentWorkout(workouts[position])

                //navigate to workout detail
                findNavController().navigate(R.id.action_workoutFragment_to_workoutDetailFragment)
            }

            //set the listener for the delete workout button. Queues a warning message before the user deletes the exercise
            holder.view.findViewById<Button>(R.id.deleteWorkoutButton).setOnClickListener {
                val builder1 = AlertDialog.Builder(context)
                builder1.setMessage(resources.getString(R.string.delete_current_exercise_message, workouts[position].workoutName))
                builder1.setCancelable(false)

                builder1.setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    model.removeWorkout(workouts[position])

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
        inner class WorkoutViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){




            }


        }
    }
}
