package com.example.hydroboost.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import com.example.hydroboost.R

class SharedPreferences(context : Context, attrs: AttributeSet? = null) : SharedPreferences {
    private val sharedPreferencesSettings = context.getSharedPreferences(
        context.getString(R.string.reminder_settings),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesCustomReminders  = context.getSharedPreferences(
        context.getString(R.string.custom_reminders_settings),
        Context.MODE_PRIVATE
    )
    private val sharedPreferencesEditor = sharedPreferencesSettings?.edit()
    override fun getAll(): MutableMap<String, *> {
        TODO("Not yet implemented")
    }

    override fun getString(p0: String?, p1: String?): String? {
        TODO("Not yet implemented")
    }

    override fun getStringSet(p0: String?, p1: MutableSet<String>?): MutableSet<String>? {
        TODO("Not yet implemented")
    }

    override fun getInt(p0: String?, p1: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getLong(p0: String?, p1: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getFloat(p0: String?, p1: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getBoolean(p0: String?, p1: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

//    fun setReminderSettings(style: String,startTime: String, endTime: String){
////        sharedPreferencesEditor.putString("startTime", gson.toJson())
//    }

//    fun getReminderSettings(): Any {
//        return sharedPreferencesSettings
//    }


}