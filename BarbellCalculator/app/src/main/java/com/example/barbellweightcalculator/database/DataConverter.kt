package com.example.barbellweightcalculator.database

import androidx.room.TypeConverter
import com.example.barbellweightcalculator.datatypes.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//This class converts the data in the SQL into Jsons
class DataConverter {

    //converts the json string of a list of exercises into a list of exercises
    @TypeConverter
    fun exercisesFromString(value: String): MutableList<Exercise> {
        val list = object: TypeToken<MutableList<Exercise>>(){}.type
        return Gson().fromJson(value, list)
    }

    //converts the list of exercises into a json string
    @TypeConverter
    fun stringFromExercises(element: MutableList<Exercise>): String {
        val gson = Gson()
        return gson.toJson(element)
    }
}
