package com.example.barbellweightcalculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.barbellweightcalculator.R

//Acts as the fragment that serves as the title screen
class TitleFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_title, container, false)

    }

    //creates the listeners for the buttons
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //references to the buttons in the view
        val calcBut = view.findViewById<Button>(R.id.calculateButton)
        val workoutBut = view.findViewById<Button>(R.id.workoutButton)
        val liftBut = view.findViewById<Button>(R.id.calculateLiftButton)

        //navigate to weight combination fragment if button is clicked
        calcBut.setOnClickListener {

            findNavController().navigate(R.id.action_titleFragment_to_weightFragment)

        }

        //navigate to workout list fragment if button is clicked
        workoutBut.setOnClickListener {

            findNavController().navigate(R.id.action_titleFragment_to_workoutFragment)
        }


        //navigate to lift fragment if button is clicked
        liftBut.setOnClickListener {
            findNavController().navigate(R.id.action_titleFragment_to_liftFragment)
        }

    }
}