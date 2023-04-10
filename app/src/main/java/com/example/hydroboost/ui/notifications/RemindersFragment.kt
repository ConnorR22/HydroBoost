package com.example.hydroboost.ui.notifications

/**
 * Reminders Fragment to display interval settings set by the user, and adjust the time frame
 * to receive notifications
 * @author: Connor Russell
 * @date: 2023/04/05
 * @version: 1.0.0
 */

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.hydroboost.R
import com.example.hydroboost.ui.SharedPreferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RemindersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RemindersFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var style: String? = null
    private var startTime: String? = null
    private var endTime: String? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        sharedPreferences = SharedPreferences(requireContext())

        val intervals = resources.getStringArray(R.array.intervals)
        val t=inflater.inflate(R.layout.fragment_reminders, container, false)
        val spinner = t.findViewById<Spinner>(R.id.intervalSelect)
        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.spinner_item, intervals) } as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                style = parent?.getItemAtPosition(position).toString()
            }
        }

        val tpStart = t.findViewById<TimePicker>(R.id.startTimePicker)
        val tpEnd = t.findViewById<TimePicker>(R.id.endTimePicker)
        val button = t.findViewById<Button>(R.id.saveSettings)

        button.setOnClickListener {
            startTime = "" + tpStart.hour + ":" + tpStart.minute
            endTime = "" + tpEnd.hour + ":" + tpEnd.minute
            saveReminder()
        }

        val notificationNav = t.findViewById<ImageButton>(R.id.notifications)
        notificationNav.setOnClickListener {
            val fragment = NotificationsFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        val addButton = t.findViewById<ImageButton>(R.id.customReminderNav)
        addButton.setOnClickListener {
            val fragment = CustomReminderFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        // Load saved setting values
        loadSettings(spinner, tpStart, tpEnd)

        return t
    }

    private fun loadSettings(spinner: Spinner, tpStart: TimePicker, tpEnd: TimePicker) {

        val sharedPreferencesSettings = context?.getSharedPreferences(
            context!!.getString(R.string.reminder_settings),
            Context.MODE_PRIVATE
        )

        if (sharedPreferencesSettings != null) {
            style = sharedPreferencesSettings.getString("style", "")
            when (style) {
                "Every 30 Minutes" -> spinner.setSelection(0)
                "Every Hour" -> spinner.setSelection(1)
                "Every 2 Hours" -> spinner.setSelection(2)
            }

            startTime = sharedPreferencesSettings.getString("startTime", "")
            val times1 = startTime?.split(":")

            if (startTime != "" && times1 != null) {
                tpStart.hour = times1.elementAt(0).toInt()
                tpStart.minute = times1.elementAt(1).toInt()
            }

            endTime = sharedPreferencesSettings.getString("endTime", "")
            val times2 = endTime?.split(":")
            if (endTime != "" && times2 != null) {
                tpEnd.hour = times2.elementAt(0).toInt()
                tpEnd.minute = times2.elementAt(1).toInt()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment notifications.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RemindersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun saveReminder() {

        val sharedPreferencesSettings = context?.getSharedPreferences(
            context!!.getString(R.string.reminder_settings),
            Context.MODE_PRIVATE
        )
        val sharedPreferencesEditor = sharedPreferencesSettings?.edit()

        // write all the data entered by the user in SharedPreference and apply
        if (sharedPreferencesEditor != null) {
            sharedPreferencesEditor.putString("style", style)
            sharedPreferencesEditor.putString("startTime", startTime)
            sharedPreferencesEditor.putString("endTime", endTime)
            sharedPreferencesEditor.apply()
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}