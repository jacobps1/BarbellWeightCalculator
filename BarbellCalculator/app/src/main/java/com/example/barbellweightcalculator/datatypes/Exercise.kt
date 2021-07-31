package com.example.barbellweightcalculator.datatypes

//represent the exercise object that stores the combinations, bar stats, and exercise name
data class Exercise(var list: ArrayList<Float>, var name: String, var weight: Float, var barIncluded: Boolean, var bar: Float) {

    //prints the object as a formatted string
    override fun toString(): String{

        if (list.size > 1) {
            var combos: String = list[0].toString() + ", "
            for (element in 1 until list.size - 1) {
                combos += list[element].toString() + ", "
            }
            combos += list[list.size - 1].toString()
            return combos
        }
        if (list.size == 0){
            return "No Weight Plates"
        }
        return list[0].toString()
    }
}
