package com.example.hydroboost.ui

import android.content.Context
import android.util.AttributeSet
import com.example.hydroboost.R
import java.text.SimpleDateFormat
import java.util.*

class SharedPreferences(context : Context, attrs: AttributeSet? = null) {
    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesEditor = sharedPreferences?.edit()
    private val dateTimeFormatter = SimpleDateFormat(context.getString(R.string.date_time_format))

    public fun getAll(): MutableMap<String, *> {
        return sharedPreferences.all
    }

    public fun getAllWaterLogs(): MutableMap<String, *> {
        val temp = getAll()
        temp.remove("WATER_GOAL")
        return temp
    }

    public fun getWaterGoal(): Int {
        //This returns 0 if "WATER_GOAL" has been accidentally removed
        return sharedPreferences.getInt("WATER_GOAL", 0)
    }

    public fun getDescendingDates(): List<Pair<String, *>> {
        val sharedPreferencesList : List<Pair <String, *>> = getAll().toList()
        return sharedPreferencesList.sortedByDescending { it.first }
    }

    public fun add(key : String, value : Int) {
        sharedPreferencesEditor?.putInt(key, value)
        sharedPreferencesEditor?.apply()
    }

    public fun remove(key : String) {
        sharedPreferencesEditor?.remove(key)
        sharedPreferencesEditor?.apply()
    }

    public fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        return dateTimeFormatter.format(dateTime)
    }

    public fun getWaterLogsToday(): MutableMap<String, *> {
        val currentDate = getCurrentDateTime().substring(0, 10)
        val allWaterLogs = getAllWaterLogs()

        val todayWaterLogs : MutableMap<String, Any?> = mutableMapOf()
        for ((dateTime, value) in allWaterLogs)
            if (dateTime.substring(0, 10) == currentDate)
                todayWaterLogs[dateTime] = value

        return todayWaterLogs
    }

    public fun getWaterDrankToday(): Int {
        val waterLogsToday = getWaterLogsToday()
        var amount : Int = 0

        for ((dateTime, value) in waterLogsToday)
            amount = amount + value as Int ?: 0

        return amount
    }

    public fun getPercentageOfGoalDrank(): Int {
        val waterDrankToday = getWaterDrankToday()
        val waterGoal = getWaterGoal()
        val percentage = ((waterDrankToday.toFloat() / waterGoal.toFloat()) * 100).toInt()

        if (percentage > 100)
            return 100

        return percentage
    }

    private val sharedPreferencesSettings = context.getSharedPreferences(
        context.getString(R.string.reminder_settings),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesCustomReminders  = context.getSharedPreferences(
        context.getString(R.string.custom_reminders_settings),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesEditor = sharedPreferencesSettings?.edit()

}