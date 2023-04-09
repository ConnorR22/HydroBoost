package com.example.hydroboost.ui.notifications

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.hydroboost.R
import com.example.hydroboost.ui.ModelPreferences
import com.example.hydroboost.ui.SharedPreferences
import org.json.JSONArray

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
    private var modelPreferences: ModelPreferences? = null

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
//        modelPreferences = ModelPreferences(requireContext())


        val t = inflater.inflate(R.layout.fragment_create_custom_reminder, container, false)

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

//            val fragment = CustomReminderFragment()
//            val transaction = fragmentManager?.beginTransaction()
//            transaction?.replace(R.id.frameLayout,fragment)?.commit()
        }

        return t
    }

    private fun saveReminder() {

        ModelPreferences.with(this)
        data class CustomReminder(val title: String, val style: String, val startTime: String, val endTime: String, val day: String)
        val customReminders = arrayListOf<CustomReminder>()

        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        val crEditPrefs = customReminderPreferences?.edit()

        val cr = CustomReminder(title!!, style!!, startTime!!, endTime!!, day!!)

        customReminders.add(cr)

//        if (customReminderPreferences != null) {
//            customReminders = customReminderPreferences.getString("CustomReminders", "")
//        }

        println("Saved Custom Reminder: \n" + cr)

//        val jsonList = JSONArray(customReminders)
        println(customReminders.toString())

        val mySet = customReminders.toSet()

        ModelPreferences.put(customReminders, "CUSTOM_REMINDERS")
    }

    fun getSharedPreferences(preferencesFileName: String, modePrivate: Int): SharedPreferences {
        return sharedPreferences!!
    }

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