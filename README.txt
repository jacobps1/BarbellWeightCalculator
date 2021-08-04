Implementation of Barbell Weight Calculator on Android Studio.

NOTE: Make sure to delete .idea folder for personal use.

The idea for my Barbell Weight Calculator came to me one day when I was lifting weights at my college
gym. After setting a personal record on the bench press, I wanted to calculate exactly how much weight
I had just benched. As a result, I developed an application that not only calculated lifts, but also
calculated the possible weight combinations required to reach a specific lift target. In addition to 
calculating these weights, I also developed an Android Room Database that allowed for storing the 
calculated lifts.

The application contained ViewModels that conducted the calculations and stored the database entries
so that the UI could find their references during screen rotation or other life-cycle ‘inducing’ events.
As mentioned earlier, the application also contained a Room Database that stored the user’s custom workouts
and their exercises. In addition, the application consisted of many fragments to display the different 
aspects of the app such as the title screen, the display of the calculations, and the display of the
workouts and their exercises.

Two custom data types were implemented in order to properly display and save the weight calculations.
The first data type implemented was an Exercise data type that stored the name of the exercise, the 
weight combination associated with it, a boolean that indicated whether the weight of the bar was 
included in the calculation or not, the weight of the total lift, and the weight of the bar used in 
the lift. The second data type implemented was a Workout data type that stored the name of the workout
and the list of exercises associated with the workout.

In order to calculate every possible weight combination for a certain lift weight, a combination algorithm
I had implemented prior to the project was used to go through every possible combination of the list of weights
that equaled the targeted lift weight. The algorithm incorporated the use of recursion to reduce the weight list 
in order to go through every element of the weight list. The algorithm also had to incorporate a restriction array
that kept track of the number of weights that were allowed to be used in the combinations based on user input from 
the calculation component of the application. Refer to the bottom of this file for more information about the
algorithm.


Barbell Weight Calculator Android Files:

package com.example.barbellweightcalculator.database:

DataConverter.kt - converts the data in the SQL Database into Jsons
WorkoutEntryDao.kt - represents the data access object used for queries for the Workout entry in the SQL database
WorkoutEntryRepository.kt - represents the repository that conducts the database queries and obtains its values
WorkoutRoomDatabase.kt - represents the creation of the database.


package com.example.barbellweightcalculator.datatypes:

Exercise.kt - represents the exercise object that stores the combinations, bar stats, and exercise name
Workout.kt - represents the table in the SQL database for the workout entry


package com.example.barbellweightcalculator.fragments:

CreateWorkoutFragment.kt - acts as the fragment that allows the user to create workouts
ExerciseFragment.kt - acts as the fragment that creates the exercise
LiftFragment.kt - acts as the fragment that allows the user to calculate lifts
TitleFragment.kt - acts as the fragment that serves as the title screen
WeightDetailFragment.kt - acts as the fragment that displays the weight combinations
WeightFragment.kt - acts as the fragment that allows the user to calculate combinations
WorkoutDetailFragment.kt - acts as the fragment that displays the exercises of a given workout
WorkoutFragment.kt - acts as the fragment that displays the list of workouts


package com.example.barbellweightcalculator:

CalculatorViewModel.kt - represents the ViewModel that stores the values of the Combination calculations and the Lift calculations
MainActivity.kt - represents MainActivity of the application
WorkoutViewModel.kt - represents the ViewModel that contains the repository for the workouts and exercises



CombinationSum.java Algorithm:
This algorithm uses recursion in a for loop in order to iterate through every possible combination whose
sums equals a specific target. This algorithm provides the basis for implementing the list of combinations
algorithm used in CalculatorViewModel.kt. The differences between the Java implementation and the 
CalculatorViewModel.kt implementation are that the Java version doesn't include any restrictions on the 
number of times a number can be repeated in a single combination, the Java version uses Integers, and the 
Java version uses a hardcoded list and target number.

Compile on Command Line: 
javac CombinationSum.java

Run on Command Line: 
java Combination