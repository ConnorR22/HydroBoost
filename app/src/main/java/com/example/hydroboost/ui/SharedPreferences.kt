package com.example.hydroboost.ui

import android.content.Context
import android.util.AttributeSet
import com.example.hydroboost.R
import java.text.SimpleDateFormat
import java.util.*

class SharedPreferences(context : Context, attrs: AttributeSet? = null) {
    private val sharedPreferencesSettings = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesEditor = sharedPreferencesSettings?.edit()


    fun getReminderSettings(): Any {
        return sharedPreferencesSettings
    }


}