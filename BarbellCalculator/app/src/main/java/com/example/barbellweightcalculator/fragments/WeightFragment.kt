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

//Acts as the fragment that allows the user to calculate combinations
class WeightFragment: Fragment() {

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
        model.resetWeights()
        return inflater.inflate(R.layout.fragment_weight, container, false)

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


        //get access to check boxes
        val noLimTwoHalf = view.findViewById<CheckBox>(R.id.noLimitTwoHalf)
        val noLimFive = view.findViewById<CheckBox>(R.id.noLimitFive)
        val noLimTen = view.findViewById<CheckBox>(R.id.noLimitTen)
        val noLimTwentyFive = view.findViewById<CheckBox>(R.id.noLimitTwentyFive)
        val noLimThirtyFive = view.findViewById<CheckBox>(R.id.noLimitThirtyFive)
        val noLimFortyFive = view.findViewById<CheckBox>(R.id.noLimitFortyFive)


        //get access to the button
        val calculateBut = view.findViewById<Button>(R.id.calculateWeightButton)


        //set listener on bar radio button group
        barRadio.setOnCheckedChangeListener{_, checkedId ->

            val radButton = view.findViewById<RadioButton>(checkedId)

            //if the button is checked
            if (radButton?.isChecked == true) {
                //clear whatever number is in the bar edit text
                customBar.text.clear()
                //check to see the text of the radio buttons and assign the appropriate weights
                when (radButton.text) {
                    resources.getString(R.string.forty_four) -> {
                        model.setBarWeight(44f)
                    }
                    resources.getString(R.string.thirty_three) -> {
                        model.setBarWeight(33f)
                    }
                    resources.getString(R.string.twenty_five) -> {
                        model.setBarWeight(25f)
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
                    model.setBarWeight(weight)
                }
                else {
                    //if there is no button that is checked in the radio group
                    if (barRadio.checkedRadioButtonId == -1) {
                        model.setBarWeight(0f)
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
        weightLimitListeners(noLimTwoHalf, limTwoHalf, 0)
        weightLimitListeners(noLimFive, limFive, 1)
        weightLimitListeners(noLimTen, limTen, 2)
        weightLimitListeners(noLimTwentyFive, limTwentyFive, 3)
        weightLimitListeners(noLimThirtyFive, limThirtyFive, 4)
        weightLimitListeners(noLimFortyFive, limFortyFive, 5)


        //set listener on custom weight target EditText
        weightTarget.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                //if there is valid input
                if (weightTarget.text.toString() != "") {
                    val weight = weightTarget.text.toString().toFloat()
                    model.setTargetWeight(weight)
                } else {
                    model.setTargetWeight(0f)
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
            }
        })

        //set listener on including bar radio group
        includeRadio.setOnCheckedChangeListener{_, checkedId ->

            val radButton = view.findViewById<RadioButton>(checkedId)

            //clear whatever number is in the bar
            if (radButton?.isChecked == true) {
                //check the text of the radio button
                if (radButton.text == resources.getString(R.string.include_bar_menu)) {
                    model.setInclude(true)
                    calculateBut.isEnabled = true
                } else {
                    model.setInclude(false)
                    calculateBut.isEnabled = true
                }


            }
        }

        //set the listener for the calculate button
        calculateBut.setOnClickListener {
            val status = model.readyCalculate()

            //if there is valid input then navigate to the list of combinations fragment
            if (status == 0) {
                model.calculateSums()
                findNavController().navigate(R.id.action_weightFragment_to_weightDetailFragment)
            }
            else{
                //if the bar weight given is not valid
                if (status == 1) {
                    val toast = Toast.makeText(activity, resources.getString(R.string.error_message_bar), Toast.LENGTH_LONG)
                    toast.show()
                }
                //if the target weight given is not valid
                else {
                    val toast = Toast.makeText(activity, resources.getString(R.string.error_message_target), Toast.LENGTH_LONG)
                    toast.show()
                }
            }
        }

    }

    //creates the listeners used to detect input
    private fun weightLimitListeners(limit: CheckBox, limitText: EditText?, index: Int){

        textWatchers[index] = object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                //if there is valid input
                if (limitText?.text.toString() != "") {
                    limit.isChecked = false
                    val lim = Integer.parseInt(limitText?.text.toString())
                    model.setRestrictions(lim, index)
                } else {
                    if (model.getRestriction(index) != -1){
                        Log.d("values", "will this change?")
                        model.setRestrictions(0, index)
                    }
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

        //listener for no limit checkbox
        limit.setOnCheckedChangeListener{ _, isChecked ->
            //if the box is checked
            if (isChecked){
                limitText?.text?.clear()
                model.setRestrictions(-1, index)
            }
            else {
                model.setRestrictions(0, index)
            }

        }

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