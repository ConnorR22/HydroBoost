package com.example.hydroboost.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import com.example.hydroboost.R
import com.example.hydroboost.ui.notifications.CreateCustomReminderFragment
import com.example.hydroboost.ui.notifications.CustomReminderFragment

class Adapter(private val context: Context, private val arrayList: ArrayList<String>): ArrayAdapter<String>(context,
    R.layout.custom_reminder_item,arrayList) {

    private var sharedPreferences: SharedPreferences? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.custom_reminder_item, null)


        val title : TextView = view.findViewById(R.id.reminderTitle)
        val editButton : Button = view.findViewById(R.id.editReminder)
        val deleteButton : Button = view.findViewById(R.id.deleteReminder)
        val toggle : Switch = view.findViewById(R.id.activateReminder)

        title.text = arrayList[position]

        sharedPreferences = SharedPreferences(context)
        val customReminderPreferences = context.getSharedPreferences(
            context.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        var customReminders = customReminderPreferences?.getString("CUSTOM_REMINDERS", "")
        val crEditPrefs = customReminderPreferences?.edit()

        var activeReminders = customReminderPreferences.getString("ACTIVE_REMINDERS", "")
        var crs = activeReminders?.split("|")
        for (reminder in crs!!){
            if (reminder != ""){
                if (reminder.equals(title.text.toString()))
                    toggle.isChecked = true
            }
        }

        editButton.setOnClickListener {

            // Grab the reminder, remove it from the existing reminders to be later readded to the list
            var reminderToEdit = ""
            var newReminders = ""
            var crs = customReminders?.split("|")
            for (reminder in crs!!){
                if (reminder != ""){
                    var fields = reminder.split(",")
                    if (!fields[0].equals(title.text))
                        newReminders += "|" + reminder
                    else
                        reminderToEdit = reminder
                }
            }

            if (crEditPrefs != null) {
                crEditPrefs.putString("CUSTOM_REMINDERS", newReminders)
                crEditPrefs.putString("EDIT_REMINDER", reminderToEdit)
                crEditPrefs.apply()
            }
            val fragment = CreateCustomReminderFragment()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout,fragment).commit()
        }

        deleteButton.setOnClickListener {
            var newReminders = ""
            var crs = customReminders?.split("|")
            for (reminder in crs!!){
                if (reminder != ""){
                    var fields = reminder.split(",")
                    if (!fields[0].equals(title.text))
                        newReminders += "|" + reminder
                }
            }

            if (crEditPrefs != null) {
                crEditPrefs.putString("CUSTOM_REMINDERS", newReminders)
                crEditPrefs.apply()
            }

            val fragment = CustomReminderFragment()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout,fragment).commit()
        }

        toggle.setOnClickListener {
            if (toggle.isChecked){
                var activeReminders = customReminderPreferences.getString("ACTIVE_REMINDERS", "")
                activeReminders += "|" + title.text

                if (crEditPrefs != null) {
                    crEditPrefs.putString("ACTIVE_REMINDERS", activeReminders)
                    crEditPrefs.apply()
                }
                println(activeReminders)
            } else {
                var activeReminders = customReminderPreferences.getString("ACTIVE_REMINDERS", "")
                var newReminders = ""
                var crs = activeReminders?.split("|")
                for (reminder in crs!!){
                    if (reminder != ""){
                        if (!reminder.equals(title.text.toString()))
                            newReminders += "|" + reminder
                    }
                }

                if (crEditPrefs != null) {
                    crEditPrefs.putString("ACTIVE_REMINDERS", newReminders)
                    crEditPrefs.apply()
                }
                println(newReminders)
            }
        }

        return view
    }
}