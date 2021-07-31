package com.example.barbellweightcalculator.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.barbellweightcalculator.CalculatorViewModel
import com.example.barbellweightcalculator.R

//Acts as the fragment that allows the user to calculate lifts
class LiftFragment: Fragment() {

    //reference to the CalculatorViewModel
    private val model: CalculatorViewModel by activityViewModels()

    //edit texts used for recording # of plates for the Lift fragment
    private var limTwoHalf: EditText? = null
    private var limFive: EditText? = null
    private var limTen: EditText? = null
    private var limTwentyFive: EditText? = null
    private var limThirtyFive: EditText? = null
    private var limFortyFive: EditText? = null
    //array used to store the TextWatchers for the EditTexts
    private var textWatchers: Array<TextWatcher?> = arrayOf(null, null, null, null, null, null)

    //creates the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)
        model.resetLiftWeights()
        return inflater.inflate(R.layout.fragment_lift, container, false)

    }

    //creates the listeners used to detect inputs and clicking of buttons
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get access to the radio groups in the view
        val barRadio = view.findViewById<RadioGroup>(R.id.barRadioGroup)
        val includeRadio = view.findViewById<RadioGroup>(R.id.includingBarGroup)



        //get access to the EditTexts
        val customBar = view.findViewById<EditText>(R.id.customBarNumber)

        limTwoHalf = view.findViewById(R.id.limitTwoHalf)
        limFive = view.findViewById(R.id.limitFive)
        limTen = view.findViewById(R.id.limitTen)
        limTwentyFive = view.findViewById(R.id.limitTwentyFive)
        limThirtyFive = view.findViewById(R.id.limitThirtyFive)
        limFortyFive = view.findViewById(R.id.limitFortyFive)

        val weightTarget = view.findViewById<EditText>(R.id.weightTarget)



        //get access to the button
        val calculateBut = view.findViewById<Button>(R.id.saveLiftAsExercise)


        //set listener on bar radio button group
        barRadio.setOnCheckedChangeListener{_, checkedId ->

            val radButton = view.findViewById<RadioButton>(checkedId)

            //if a radio button is selected
            if (radButton?.isChecked == true) {
                //clear whatever number is in the bar
                customBar.text.clear()

                //check to see the text of the radio buttons and assign the appropriate weights
                when (radButton.text) {
                    resources.getString(R.string.forty_four) -> {
                        model.setLiftBarWeight(44f)
                    }
                    resources.getString(R.string.thirty_three) -> {
                        model.setLiftBarWeight(33f)
                    }
                    resources.getString(R.string.twenty_five) -> {
                        model.setLiftBarWeight(25f)
                    }
                }

            }
        }

        //set listener on custom bar weight EditText
        customBar.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //if there is valid input
                if (customBar.text.toString() != "") {
                    //clear the check of the radio buttons
                    barRadio.clearCheck()
                    val weight = customBar.text.toString().toFloat()
                    //set the weight to what was entered
                    model.setLiftBarWeight(weight)
                }
                else {
                    //if there is no button that is checked in the radio group
                    if (barRadio.checkedRadioButtonId == -1) {
                        model.setLiftBarWeight(0f)
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing


            }
        })

        //create the text edit listeners
        weightLimitListeners(limTwoHalf, 0)
        weightLimitListeners(limFive, 1)
        weightLimitListeners(limTen, 2)
        weightLimitListeners(limTwentyFive, 3)
        weightLimitListeners(limThirtyFive, 4)
        weightLimitListeners(limFortyFive, 5)


        //observer that displays the value of the lift in the EditText as it updates
        model.liftTotal.observe(viewLifecycleOwner, {totalLift ->

            weightTarget.setText(totalLift.toString())
        })


        //set listener on including bar radio group
        includeRadio.setOnCheckedChangeListener{_, checkedId ->

            val radButton = view.findViewById<RadioButton>(checkedId)

            //check to see if a radio button is checked
            if (radButton?.isChecked == true) {

                //check the text of the radio button
                if (radButton.text == resources.getString(R.string.include_bar_menu)) {
                    model.selectedBar(true, clickedButton = true)
                    calculateBut.isEnabled = true
                } else {
                    model.selectedBar(false, clickedButton = true)
                    calculateBut.isEnabled = true
                }


            }
        }

        //set the listener for the save lift button
        calculateBut.setOnClickListener {
            //if there is valid input
            when (model.validInput) {
                0 -> {
                    findNavController().navigate(R.id.action_liftFragment_to_exerciseFragment)
                }
                //if the bar weight is not valid
                1 -> {
                    val toast = Toast.makeText(activity, resources.getString(R.string.error_message_bar), Toast.LENGTH_LONG)
                    toast.show()
                }
                //if the lift total is not valid
                else -> {
                    val toast = Toast.makeText(activity, resources.getString(R.string.error_message_lift), Toast.LENGTH_LONG)
                    toast.show()
                }
            }


        }

    }
    //creates the listeners used to detect input
    private fun weightLimitListeners(limitText: EditText?, index: Int){

        textWatchers[index] = object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

                //if there is valid input
                if (limitText?.text.toString() != "") {
                    val lim = Integer.parseInt(limitText?.text.toString())
                    model.enterNumberPlate(lim, index)
                } else {
                    Log.d("values", "will this change?")
                    model.enterNumberPlate(0, index)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
                //barRadio.clearCheck()

            }
        }

        limitText?.addTextChangedListener(textWatchers[index])


    }

    //when the view is paused, remove the TextWatchers on the EditTexts
    override fun onPause() {
        super.onPause()
        limTwoHalf?.let { removeWatcher(textWatchers[0], it) }
        limFive?.let { removeWatcher(textWatchers[1], it) }
        limTen?.let { removeWatcher(textWatchers[2], it) }
        limTwentyFive?.let { removeWatcher(textWatchers[3], it) }
        limThirtyFive?.let { removeWatcher(textWatchers[4], it) }
        limFortyFive?.let { removeWatcher(textWatchers[5], it) }

    }
    //remove the TextWatcher
    private fun removeWatcher(watcher: TextWatcher?, text: EditText){
        if (watcher != null){
            text.removeTextChangedListener(watcher)
        }
    }
}
