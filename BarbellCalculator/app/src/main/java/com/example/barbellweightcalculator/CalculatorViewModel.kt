package com.example.barbellweightcalculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.barbellweightcalculator.datatypes.Exercise
import kotlin.collections.ArrayList

//represents the ViewModel that stores the values of the Combination calculations and the Lift calculations
class CalculatorViewModel(application: Application): AndroidViewModel(application) {

    //represents the weight of the bar
    private var barWeight: Float = 0f

    //represents the restrictions on the number of plates
    private var restrictions = intArrayOf(0, 0, 0, 0, 0, 0)

    //represents the weights available
    private var weightArray = floatArrayOf(2.5f, 5.0f, 10.0f, 25.0f, 35.0f, 45.0f)


    //represents the target weight
    private var target: Float = 0f

    //represents whether the bar should be included in the target weight or not
    private var includeBar: Boolean = true


    //represents the list of Exercises/Weight Combinations
    private var combinations: MutableList<Exercise> = arrayListOf()

    private var _comboList = MutableLiveData<List<Exercise>>()
    val comboList: LiveData<List<Exercise>>
        get() = _comboList

    ////////////////////////////////////////////////////////////////////////////////////////

    //variables used to calculate the weights of lifts

    //represents the number of plates
    private var numberOfWeights = intArrayOf(0, 0, 0, 0, 0, 0)

    //whether or not the bar needs to be included for the lift calculation
    private var includeLiftBar: Boolean = true


    //represents the total weight of the lift
    private var _liftTotal = MutableLiveData<Float>()
    val liftTotal: LiveData<Float>
        get() = _liftTotal


    //represents the weight of the bar
    private var liftBarWeight = 0f

    //checks to see if the user has selected to include bar or not
    private var selectedBar: Boolean = false

    //check to see if there is valid input: 0 is valid, 1 means bar weight is wrong, 2 means there is no weights
    var validInput: Int = 0


    /////////////////////////////////////////////////////////////////////

    //represents the exercise that will be created
    private lateinit var selectedExercise: Exercise

    /////////////////////////////////////////////////////////////////////

    //functions use to calculate the combination of weights

    //gets the restriction of the weight at the specified index
    fun getRestriction(index: Int): Int{
        return restrictions[index]
    }

    //resets back to the initial values for combination calculation
    fun resetWeights(){
        barWeight = 0f

        //represents the restrictions on the number of plates
        restrictions = intArrayOf(0, 0, 0, 0, 0, 0)

        //represents the weights available
        weightArray = floatArrayOf(2.5f, 5.0f, 10f, 25f, 35f, 45f)


        //represents the target weight
        target = 0f

        //represents whether the bar should be included in the target weight or not
        includeBar = true

        combinations.clear()
    }

    //sets the selected exercise
    fun setSelectedExercise(exercise: Exercise){
        selectedExercise = exercise
    }

    //gets the selected exercise
    fun getSelectedExercise(): Exercise{
        return selectedExercise
    }

    //gets the weight of the bar
    fun getBarWeight(): Float{
        return barWeight
    }

    //sets the weight of the bar
    fun setBarWeight(weight: Float){
        barWeight = weight
    }

    //sets the restrictions of the weight at the given index (-1 means no restriciton)
    fun setRestrictions(numWeight: Int, index: Int){

        //if there is a vaild number divide it by 2 to account for both sides of the bar
        if (numWeight >= 0) {
            restrictions[index] = numWeight / 2
        }
        //no restriction
        else {
            restrictions[index] = numWeight
        }

    }

    //set the target weight for the combination calculation
    fun setTargetWeight(tWeight: Float){
        target = tWeight
    }

    //set the status of whether the bar should be included or not
    fun setInclude(status: Boolean){
        includeBar = status
    }

    //check to see if the combination is ready to be calculated
    fun readyCalculate(): Int{


        var temp = target
        //if the weight of the bar matters
        if (includeBar){
            temp -= barWeight
        }
        temp /= 2

        //if the weight of the bar is 0 and the weight does matter
        if (barWeight == 0f && includeBar) {
            return 1
        }

        //check to see if the target weight is less than the weight of the bar
        if (temp < 0f || temp == 0f && !includeBar){
            return 2
        }

        //calculation is ready to be calculated
        return 0

    }

    //filter the list of combinations in the combinations list fragment
    fun filterWeights(filter: Float){
        //if the filter is valid
        if (filter != 0f){
            val tempCombo: MutableList<Exercise> = arrayListOf()
            for (combo in combinations){
                //if the element is in the list, then add the entire list
                if (combo.list.contains(filter)){
                    tempCombo.add(combo)
                }
            }
            _comboList.value = tempCombo
        }
        //show all the combinations
        else {
            _comboList.value = combinations
        }
    }

    //calculate the total possible weight combinations
    fun calculateSums(){


        var temp = target
        if (includeBar){
            temp-= barWeight
        }
        temp /= 2


        val sums = ArrayList<Float>()

        //make a temporary list of the weights
        val list = ArrayList<Float>()
        for (element in weightArray){
            list.add(element)
        }

        //make a copy of the restrictions
        val restrictCopy = intArrayOf(restrictions[0], restrictions[1], restrictions[2], restrictions[3], restrictions[4], restrictions[5])

        //find the combinations
        findSums(list, temp, sums, restrictCopy)

        _comboList.value = combinations



    }



    //wrapper function
    private fun findSums(weightArray: ArrayList<Float>,
        target: Float,
        currentSums: ArrayList<Float>,
        restrictCopy: IntArray
    ) {

        //represent the current index of the restricted weights array
        val restrictedIndex = 0

        //clear the combinations calculated previously
        combinations.clear()
        findSumsHelper(weightArray, target, currentSums, restrictCopy, restrictedIndex)


    }

//finds all of the possible combinations needed to achieve the target weight
    private fun findSumsHelper(
        weightArray: ArrayList<Float>, target: Float,
        currentSums: ArrayList<Float>,
        restrictCopy: IntArray,
        restrictedIndex: Int
    ) {

        var restrictedI = restrictedIndex

        var currentSum = 0f

        //get the current sum of the list passed in the method
        for (value in currentSums) {
            currentSum += value
        }
        //if the sum is equal to the target
        if (currentSum == target) {
            //create a temporary exercise variable and add it to the combinations list
            val temp = Exercise(currentSums, "Placeholder", this.target, this.includeBar, this.barWeight)
            combinations.add(temp)

            return

        }
        //if the sum has surpassed the target weight then back out of the recursion
        if (currentSum > target) {

            return
        }

        //loop through the weight array
        for (i in weightArray.indices) {

            //variable that keeps track of whether or not an extra variable is added in the weight array
            var extraValue = false

            //represents the reduced list that will be passed through the recursion
            val reducedList = ArrayList<Float>()

            //gets the weight value at the given index
            val entry = weightArray[i]

            //gets the restricted value at the given index
            val restriction = restrictCopy[restrictedI]

            //subtract 1 from restricted value to account for using the value
            restrictCopy[restrictedI] = restriction - 1

            //if there is another value that can still be used from the restricted then add it to the list
            if ((restriction - 1) != 0) {
                reducedList.add(entry)

                //indicate that an extra value was added during the pass of the loop
                extraValue = true
            }
            else {
                restrictedI++
            }


            //create the new list that will be passed in after current value is done
            for (j in i + 1 until weightArray.size) {
                reducedList.add(weightArray[j])
            }
            //create a new list that represents the way of getting the sum from the new set of values
            val partialRec = ArrayList<Float>()
            partialRec.addAll(currentSums)


            //check to see if the current number has a restriction of 0 meaning that it shouldn't be incorporated in the sum
            if (restriction != 0) {
                //add it to the sum
                partialRec.add(entry)

                //create copy of the restrictions
                val updatedRestrictions  = intArrayOf(restrictCopy[0], restrictCopy[1], restrictCopy[2], restrictCopy[3], restrictCopy[4], restrictCopy[5])

                //recursively call the method
                findSumsHelper(
                    reducedList, target,
                    partialRec,
                    updatedRestrictions,
                    restrictedI
                )
            }


            //if an extra number was added to the list
            if (extraValue) {

                //remove the extra number
                reducedList.removeAt(0)


                //restore the index back to the way it was from before
                restrictCopy[restrictedI] = restriction

            } else {

                //restore restrictions of number back to normal
                restrictCopy[restrictedI - 1] = restriction
                //decrease the index to the current number's index
                restrictedI--

            }
            //increment restriction index by 1 for the next number
            restrictedI++


        }

    }

    //////////////////////////////////////////////////////////////////////////////

    //methods for calculating the lift

    //enters the number of plates used to calculate the lift
    fun enterNumberPlate(numWeight: Int, index:Int){
        numberOfWeights[index] = numWeight
        calculateLift()
    }
    //set the weight of the lift bar
    fun setLiftBarWeight(weight: Float){
        liftBarWeight = weight
        calculateLift()
    }
    //update the status of the bar
    fun selectedBar(status: Boolean, clickedButton: Boolean){
        includeLiftBar = status
        selectedBar = clickedButton
        calculateLift()
    }

    //calculate the lift
    private fun calculateLift(){
        //if the user has selected to include or not include the bar
        if (selectedBar) {

                var sum = 0f
                val list = ArrayList<Float>()
                //calculate the sum of the weight plates
                for ((index, weight) in numberOfWeights.withIndex()) {
                    //add the weights into the list
                    for (i in 0 until weight) {
                        sum += weightArray[index]
                        list.add(weightArray[index])
                    }
                }
                //multiply by 2 to account for both sides
                sum *= 2

                //add the bar if the user requests it
                if (includeLiftBar) {
                    if (liftBarWeight != 0f) {
                        sum += liftBarWeight
                    }
                    else {
                        validInput = 1
                        return
                    }
                }
                else {
                    //if the sum is 0 and the bar is not included
                    if (sum == 0f){
                        validInput = 2
                        _liftTotal.postValue(sum)
                        return
                    }
                }

                _liftTotal.postValue(sum)
                selectedExercise = Exercise(list, "", sum, includeLiftBar, liftBarWeight)
                validInput = 0
        }

    }

    //reset the values of the weights used for the lift
    fun resetLiftWeights(){
        numberOfWeights = intArrayOf(0, 0, 0, 0, 0, 0)

        //whether or not the bar needs to be included for the lift calculation
        includeLiftBar = true


        //represents the weight of the bar
        liftBarWeight = 0f

        //checks to see if the user has selected to include bar or not
        selectedBar = false

        validInput = 2
        _liftTotal.value = 0f
    }
}