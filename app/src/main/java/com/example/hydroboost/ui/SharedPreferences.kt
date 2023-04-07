package com.example.hydroboost.ui

import android.content.Context
import android.util.AttributeSet
import com.example.hydroboost.R

class SharedPreferences(context : Context, attrs: AttributeSet? = null) {
    private val sharedPreferencesSettings = context.getSharedPreferences(
        context.getString(R.string.reminder_settings),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesEditor = sharedPreferencesSettings?.edit()

    fun setReminderSettings(style: String,startTime: String, endTime: String){
//        sharedPreferencesEditor.putString("startTime", gson.toJson())
    }

    fun getReminderSettings(): Any {
        return sharedPreferencesSettings
    }


}