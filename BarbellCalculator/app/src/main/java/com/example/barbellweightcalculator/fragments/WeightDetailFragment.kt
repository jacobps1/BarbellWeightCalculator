package com.example.barbellweightcalculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barbellweightcalculator.CalculatorViewModel
import com.example.barbellweightcalculator.R
import com.example.barbellweightcalculator.datatypes.Exercise

//Acts as the fragment that displays the weight combinations
class WeightDetailFragment: Fragment() {

    //reference to the RecyclerView adapter
    private val adapter = CombinationListAdapter()
    //reference to CalculatorViewModel
    private val model: CalculatorViewModel by activityViewModels()

    //creates the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        setHasOptionsMenu(true)

        //observer that sets the list of combinations into the adapter
        model.comboList.observe(viewLifecycleOwner, { combinations ->
            adapter.setCombos(combinations)
        })

        return inflater.inflate(R.layout.fragment_weightdetail, container, false)

    }

    //sets the listener for the radio group that filters the combinations
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.exerciseRecycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val filterRadio = view.findViewById<RadioGroup>(R.id.filterRadioGroup)


        filterRadio.setOnCheckedChangeListener { _, checkedId ->
            val radButton = view.findViewById<RadioButton>(checkedId)

            //checks to see the text of the selected radio button to determine how to filter the combinations
            when (radButton?.text) {
                resources.getString(R.string.forty_five) -> {
                    model.filterWeights(45.0f)
                }
                resources.getString(R.string.thirty_five) -> {
                    model.filterWeights(35.0f)
                }
                resources.getString(R.string.twenty_five) -> {
                    model.filterWeights(25.0f)
                }
                resources.getString(R.string.ten) -> {
                    model.filterWeights(10.0f)
                }
                resources.getString(R.string.five) -> {
                    model.filterWeights(5.0f)
                }
                resources.getString(R.string.two_half) -> {
                    model.filterWeights(2.5f)
                }
                //show all button
                else -> {
                    model.filterWeights(0.0f)
                }
            }
        }
    }

    //inner class that represents the adapter for the RecyclerView
    inner class CombinationListAdapter :
        RecyclerView.Adapter<CombinationListAdapter.ComboViewHolder>(){

        //represents the list of combinations as exercises
        private var combos = emptyList<Exercise>()


        //sets the list of combinations displayed in the RecyclerView
        internal fun setCombos(combos: List<Exercise>) {
            this.combos = combos

            notifyDataSetChanged()
        }

        //gets the number of items in the adapter
        override fun getItemCount(): Int {

            return combos.size
        }


        //creates the ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboViewHolder {


            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_weightcombination, parent, false)
            return ComboViewHolder(v)
        }

        //assigns the views within the ViewHolder values
        override fun onBindViewHolder(holder: ComboViewHolder, position: Int) {


            //check to see if the bar is included in the combination calculation
            val includeStatus = if (combos[position].barIncluded){
                resources.getString(R.string.include_bar, combos[position].bar)
            } else {
                resources.getString(R.string.not_include)
            }

            holder.view.findViewById<TextView>(R.id.workoutName).text = resources.getString(R.string.combo_label, combos[position].weight)
            holder.view.findViewById<TextView>(R.id.numberExercisesLabel).text = includeStatus

            holder.view.findViewById<TextView>(R.id.weightCombo).text = combos[position].toString()

            //navigate to create exercise fragment if button is clicked
            holder.view.findViewById<Button>(R.id.viewExerciseButton).setOnClickListener {
                model.setSelectedExercise(combos[position])
                findNavController().navigate(R.id.action_weightDetailFragment_to_exerciseFragment)
            }
        }

        //custom view holder that represents the view of an individual item in the RecyclerView
        inner class ComboViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){




            }


        }
    }
}