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


}