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
        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        var customReminders = customReminderPreferences?.getString("CUSTOM_REMINDERS", "")


        editButton.setOnClickListener {
            val fragment = CreateCustomReminderFragment()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
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

            println(newReminders)
            val crEditPrefs = customReminderPreferences?.edit()

            if (crEditPrefs != null) {
                crEditPrefs.putString("CUSTOM_REMINDERS", newReminders)
                crEditPrefs.apply()
            }

            val fragment = CustomReminderFragment()
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        return view
    }
}