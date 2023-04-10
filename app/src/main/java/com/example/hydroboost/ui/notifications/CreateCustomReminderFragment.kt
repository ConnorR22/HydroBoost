package com.example.hydroboost.ui.notifications

/**
 * Create Custom Reminders fragment is an activity that allows users to create, edit and
 * store custom reminders to be displayed on the Custom Reminders fragment
 * @author: Connor Russell
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.hydroboost.R
import com.example.hydroboost.ui.SharedPreferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateCustomReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateCustomReminderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var title: String? = null
    private var style: String? = null
    private var startTime: String? = null
    private var endTime: String? = null
    private var day: String? = null



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

        sharedPreferences = SharedPreferences(requireContext())


        val t = inflater.inflate(R.layout.fragment_create_custom_reminder, container, false)

        val backButton = t.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val fragment = CustomReminderFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        val intervals = resources.getStringArray(R.array.intervals)
        val spinnerReminder = t.findViewById<Spinner>(R.id.intervalSelect)
        spinnerReminder?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.spinner_item, intervals) } as SpinnerAdapter
        spinnerReminder?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                style = parent?.getItemAtPosition(position).toString()
            }
        }

        val days = resources.getStringArray(R.array.daysOfTheWeek)

        val spinnerDays = t.findViewById<Spinner>(R.id.selectDay)
        spinnerDays?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.spinner_item, days) } as SpinnerAdapter
        spinnerDays?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                day = parent?.getItemAtPosition(position).toString()
            }
        }

        val titleText = t.findViewById<EditText>(R.id.customTitle)
        val tpStart = t.findViewById<TimePicker>(R.id.startTimePicker)
        val tpEnd = t.findViewById<TimePicker>(R.id.endTimePicker)
        val addButton = t.findViewById<Button>(R.id.saveReminder)

        addButton.setOnClickListener {
            title = titleText.text.toString()
            startTime = "" + tpStart.hour + ":" + tpStart.minute
            endTime = "" + tpEnd.hour + ":" + tpEnd.minute
            saveReminder()

            val fragment = CustomReminderFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        val reminder = customReminderPreferences!!.getString("EDIT_REMINDER", "")
        if (reminder != "")
            loadReminder(backButton, titleText, spinnerReminder, tpStart, tpEnd, spinnerDays)

        return t
    }

    private fun loadReminder(
        backButton: ImageButton,
        titleText: EditText,
        spinnerReminder: Spinner,
        tpStart: TimePicker,
        tpEnd: TimePicker,
        spinnerDays: Spinner
    ) {
        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        val reminder = customReminderPreferences!!.getString("EDIT_REMINDER", "")!!.split(",")

        backButton.setVisibility(View.INVISIBLE)

        title = reminder[0]
        titleText.setText(title)

        style = reminder[1]
        when (style) {
            "Every 30 Minutes" -> spinnerReminder.setSelection(0)
            "Every Hour" -> spinnerReminder.setSelection(1)
            "Every 2 Hours" -> spinnerReminder.setSelection(2)
        }

        startTime = reminder[2]
        val times1 = startTime?.split(":")

        if (startTime != "" && times1 != null) {
            tpStart.hour = times1.elementAt(0).toInt()
            tpStart.minute = times1.elementAt(1).toInt()
        }

        endTime = reminder[3]
        val times2 = endTime?.split(":")
        if (endTime != "" && times2 != null) {
            tpEnd.hour = times2.elementAt(0).toInt()
            tpEnd.minute = times2.elementAt(1).toInt()
        }

        day = reminder[4]
        when (day) {
            "Sunday"    -> spinnerDays.setSelection(0)
            "Monday"    -> spinnerDays.setSelection(1)
            "Tuesday"   -> spinnerDays.setSelection(2)
            "Wednesday" -> spinnerDays.setSelection(3)
            "Thursday"  -> spinnerDays.setSelection(4)
            "Friday"    -> spinnerDays.setSelection(5)
            "Saturday"  -> spinnerDays.setSelection(6)
        }

        val crEditPrefs = customReminderPreferences.edit()
        crEditPrefs!!.putString("EDIT_REMINDER", "")
        crEditPrefs.apply()
    }

    private fun saveReminder() {

        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        val crEditPrefs = customReminderPreferences?.edit()

        var customReminders = customReminderPreferences?.getString("CUSTOM_REMINDERS", "")

        customReminders += "|" + title + "," + style + "," + startTime + "," + endTime + "," + day

        if (crEditPrefs != null) {
            crEditPrefs.putString("CUSTOM_REMINDERS",customReminders)
            crEditPrefs.apply()
        }
    }

//    fun getSharedPreferences(preferencesFileName: String, modePrivate: Int): SharedPreferences {
//        return sharedPreferences!!
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateCustomReminderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateCustomReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}