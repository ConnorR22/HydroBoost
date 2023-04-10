package com.example.hydroboost.data

/**
 * A Kotlin class used to access local device SharedPreferences.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.content.Context
import android.util.AttributeSet
import com.example.hydroboost.R
import java.text.SimpleDateFormat
import java.util.*

class SharedPreferencesBen(context : Context, attrs: AttributeSet? = null) {
    //Define SharedPreferences object
    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    //Define SharedPreferences Editor
    private val sharedPreferencesEditor = sharedPreferences?.edit()
    private val dateTimeFormatter = SimpleDateFormat(context.getString(R.string.date_time_format))

    /**
     * A function used to get all entries from the SharedPreferences.
     * @return: A MutableMap of all entries.
     */
    public fun getAll(): MutableMap<String, *> {
        return sharedPreferences.all
    }

    /**
     * A function used to get all entries from the SharedPreferences, except for the WATER_GOAL.
     * @return: A MutableMap of all entries, except for the WATER_GOAL.
     */
    public fun getAllWaterLogs(): MutableMap<String, *> {
        val temp = getAll()
        temp.remove("WATER_GOAL")
        return temp
    }

    /**
     * A function that returns an integer representing the WATER_GOAL.
     * @return: The WATER_GOAL as an Int.
     */
    public fun getWaterGoal(): Int {
        //This returns 0 if "WATER_GOAL" has been accidentally removed
        return sharedPreferences.getInt("WATER_GOAL", 0)
    }

    /**
     * A function that gets all date, water log pairs in descending order by date.
     * @return: A List of date water log pairs.
     */
    public fun getDescendingDates(): List<Pair<String, *>> {
        val sharedPreferencesList : List<Pair <String, *>> = getAll().toList()
        return sharedPreferencesList.sortedByDescending { it.first }
    }

    /**
     * A function used to add a String, Int pair in the form of (date, water amount) to
     * SharedPreferences.
     * @param key: A dateTime String.
     * @param value: A water consumed amount Int.
     */
    public fun add(key : String, value : Int) {
        sharedPreferencesEditor?.putInt(key, value)
        sharedPreferencesEditor?.apply()
    }

    /**
     * A function used to remove a water log instanced, based off of dateTime key.
     * @param key: A String representing the water log entry dateTime.
     */
    public fun remove(key : String) {
        sharedPreferencesEditor?.remove(key)
        sharedPreferencesEditor?.apply()
    }

    /**
     * A function that returns the current dateTime as a String.
     * @return: The dateTime String.
     */
    public fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        return dateTimeFormatter.format(dateTime)
    }

    /**
     * A function that returns a MutableMap containing all water log entries for the current date.
     * @return: A MutableMap containing (dateTime, water log amount) pairs for the current date.
     */
    public fun getWaterLogsToday(): MutableMap<String, *> {
        val currentDate = getCurrentDateTime().substring(0, 10)
        val allWaterLogs = getAllWaterLogs()

        val todayWaterLogs : MutableMap<String, Any?> = mutableMapOf()
        for ((dateTime, value) in allWaterLogs)
            if (dateTime.substring(0, 10) == currentDate)
                todayWaterLogs[dateTime] = value

        return todayWaterLogs
    }

    /**
     * A function that returns the total amount of water logged for the current date.
     * @return: An Int representing the total amount consumed for the day.
     */
    public fun getWaterDrankToday(): Int {
        val waterLogsToday = getWaterLogsToday()
        var amount : Int = 0

        for ((dateTime, value) in waterLogsToday)
            amount = amount + value as Int ?: 0

        return amount
    }

    /**
     * A function that returns the percentage of water logged for the day, relative to the
     * WATER_GOAL.
     * @return: An Int representing the percentage of the goal met, from 0 to 100.
     */
    public fun getPercentageOfGoalDrank(): Int {
        val waterDrankToday = getWaterDrankToday()
        val waterGoal = getWaterGoal()
        val percentage = ((waterDrankToday.toFloat() / waterGoal.toFloat()) * 100).toInt()

        if (percentage > 100)
            return 100

        return percentage
    }
}