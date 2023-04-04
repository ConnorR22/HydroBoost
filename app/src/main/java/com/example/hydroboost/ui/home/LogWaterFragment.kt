package com.example.hydroboost.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.hydroboost.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogWaterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogWaterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_log_water, container, false)
        view as ViewGroup

        saveLogWaterEvent(view)
        cancelLogWaterEvent(view)
        saveWaterGoalEvent(view)
        cancelWaterGoalEvent(view)

        // Inflate the layout for this fragment
        return view
    }

    private fun saveLogWaterEvent(view : View) {
        val saveLogWaterButton : Button = view.findViewById(R.id.save_log_water_button)
        saveLogWaterButton.setOnClickListener {
            //Get the amount of water consumed in ml
            val logWaterEditText : EditText = view.findViewById(R.id.log_water_edit_text)
            val waterAmount = logWaterEditText.text.toString()

            //Check to see if water amount entered when "SAVE" pressed by user.
            if (! waterAmount.isEmpty()) {
                //Define the sharedPreferences file to write to
                val sharedPreferences = activity?.getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
                val sharedPreferencesEditor = sharedPreferences?.edit()

                //Get and format the current date/time as they sharedPreferences key for water log entry
                val calendar = Calendar.getInstance()
                val dateTime = calendar.time
                val dateTimeFormatter = SimpleDateFormat(getString(R.string.date_time_format))
                val formattedDateTime = dateTimeFormatter.format(dateTime)

                //Insert dateTime key, water int pair into sharedPreferences
                sharedPreferencesEditor?.putInt(formattedDateTime, waterAmount.toInt())
                sharedPreferencesEditor?.apply()
            }

            //Move back to home fragment
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, HomeFragment())
            transaction.commit()
        }
    }

    private fun cancelLogWaterEvent(view : View) {
        val cancelLogWaterButton : Button = view.findViewById(R.id.cancel_log_water_button)
        cancelLogWaterButton.setOnClickListener {
            //Move back to home fragment
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, HomeFragment())
            transaction.commit()
        }
    }

    private fun saveWaterGoalEvent(view : View) {
        val saveWaterGoalButton : Button = view.findViewById(R.id.save_water_goal_button)
        saveWaterGoalButton.setOnClickListener {
            //Get the amount of water consumed in ml
            val waterGoalEditText : EditText = view.findViewById(R.id.water_goal_edit_text)
            val waterAmount = waterGoalEditText.text.toString()

            //Check to see if water amount entered when "SAVE" pressed by user.
            if (! waterAmount.isEmpty()) {
                //Define the sharedPreferences file to write to
                val sharedPreferences = activity?.getSharedPreferences(
                    getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
                val sharedPreferencesEditor = sharedPreferences?.edit()

                //Insert water goal Int into sharedPreferences with "WATER_GOAL" as key
                sharedPreferencesEditor?.putInt("WATER_GOAL", waterAmount.toInt())
                sharedPreferencesEditor?.apply()
            }

            //Move back to home fragment
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, HomeFragment())
            transaction.commit()
        }
    }

    private fun cancelWaterGoalEvent(view : View) {
        val cancelWaterGoalButton : Button = view.findViewById(R.id.cancel_water_goal_button)
        cancelWaterGoalButton.setOnClickListener {
            //Move back to home fragment
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, HomeFragment())
            transaction.commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogWaterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogWaterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}