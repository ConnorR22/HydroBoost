package com.example.hydroboost.ui.home

/**
 * A fragment used to log water consumption instances, and set daily intake goal.
 * These values are saved to the device locally.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.hydroboost.R
import com.example.hydroboost.data.SharedPreferences

class LogWaterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_water, container, false)
        view as ViewGroup

        //Create SharedPreferences object
        val sharedPreferences = SharedPreferences(requireContext())

        //Define events for saving and cancelling
        saveLogWaterEvent(view, sharedPreferences)
        cancelLogWaterEvent(view)
        saveWaterGoalEvent(view, sharedPreferences)
        cancelWaterGoalEvent(view)

        // Inflate the layout for this fragment
        return view
    }

    /**
     * A function used to handle a user saving a water log entry.
     * @param view: The current view
     * @param sharedPreferences: The SharedPreferences object.
     */
    private fun saveLogWaterEvent(view : View, sharedPreferences : SharedPreferences) {
        val saveLogWaterButton : Button = view.findViewById(R.id.save_log_water_button)
        saveLogWaterButton.setOnClickListener {
            //Get the amount of water consumed in ml
            val logWaterEditText : EditText = view.findViewById(R.id.log_water_edit_text)
            val waterAmount = logWaterEditText.text.toString()

            //Check to see if water amount entered when "SAVE" pressed by user, and save log.
            if (! waterAmount.isEmpty())
                sharedPreferences.add(sharedPreferences.getCurrentDateTime(), waterAmount.toInt())

            //Move back to home fragment
            returnHome()
        }
    }

    /**
     * A function used to handle a user cancelling a water log entry.
     * @param view: The current view.
     */
    private fun cancelLogWaterEvent(view : View) {
        val cancelLogWaterButton : Button = view.findViewById(R.id.cancel_log_water_button)
        cancelLogWaterButton.setOnClickListener {
            //Move back to home fragment
            returnHome()
        }
    }

    /**
     * A function used to handle a user saving a water goal entry.
     * @param view: The current view
     * @param sharedPreferences: The SharedPreferences object.
     */
    private fun saveWaterGoalEvent(view : View, sharedPreferences : SharedPreferences) {
        val saveWaterGoalButton : Button = view.findViewById(R.id.save_water_goal_button)
        saveWaterGoalButton.setOnClickListener {
            //Get the amount of water consumed in ml
            val waterGoalEditText : EditText = view.findViewById(R.id.water_goal_edit_text)
            val waterAmount = waterGoalEditText.text.toString()

            //Check to see if water amount entered when "SAVE" pressed by user, and save goal.
            if (! waterAmount.isEmpty())
                sharedPreferences.add("WATER_GOAL", waterAmount.toInt())

            //Move back to home fragment
            returnHome()
        }
    }

    /**
     * A function used to handle a user cancelling a water goal entry.
     * @param view: The current view.
     */
    private fun cancelWaterGoalEvent(view : View) {
        val cancelWaterGoalButton : Button = view.findViewById(R.id.cancel_water_goal_button)
        cancelWaterGoalButton.setOnClickListener {
            //Move back to home fragment
            returnHome()
        }
    }

    /**
     * A function to return to the HomeFragment.
     */
    private fun returnHome() {
        val manager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, HomeFragment())
        transaction.commit()
    }
}