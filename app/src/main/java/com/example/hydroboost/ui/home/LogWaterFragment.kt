package com.example.hydroboost.ui.home

/**
 * A fragment used to log water consumption instances, and set daily intake goal.
 * These values are saved to the device locally.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.hydroboost.R
import com.example.hydroboost.ui.SharedPreferences

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

        val sharedPreferences = SharedPreferences(requireContext())

        saveLogWaterEvent(view, sharedPreferences)
        cancelLogWaterEvent(view)
        saveWaterGoalEvent(view, sharedPreferences)
        cancelWaterGoalEvent(view)

        // Inflate the layout for this fragment
        return view
    }

    private fun saveLogWaterEvent(view : View, sharedPreferences : SharedPreferences) {
        val saveLogWaterButton : Button = view.findViewById(R.id.save_log_water_button)
        saveLogWaterButton.setOnClickListener {
            //Get the amount of water consumed in ml
            val logWaterEditText : EditText = view.findViewById(R.id.log_water_edit_text)
            val waterAmount = logWaterEditText.text.toString()

            //Check to see if water amount entered when "SAVE" pressed by user.
            if (! waterAmount.isEmpty())
                sharedPreferences.add(sharedPreferences.getCurrentDateTime(), waterAmount.toInt())

            //Move back to home fragment
            returnHome()
        }
    }

    private fun cancelLogWaterEvent(view : View) {
        val cancelLogWaterButton : Button = view.findViewById(R.id.cancel_log_water_button)
        cancelLogWaterButton.setOnClickListener {
            //Move back to home fragment
            returnHome()
        }
    }

    private fun saveWaterGoalEvent(view : View, sharedPreferences : SharedPreferences) {
        val saveWaterGoalButton : Button = view.findViewById(R.id.save_water_goal_button)
        saveWaterGoalButton.setOnClickListener {
            //Get the amount of water consumed in ml
            val waterGoalEditText : EditText = view.findViewById(R.id.water_goal_edit_text)
            val waterAmount = waterGoalEditText.text.toString()

            //Check to see if water amount entered when "SAVE" pressed by user.
            if (! waterAmount.isEmpty())
                sharedPreferences.add("WATER_GOAL", waterAmount.toInt())

            //Move back to home fragment
            returnHome()
        }
    }

    private fun cancelWaterGoalEvent(view : View) {
        val cancelWaterGoalButton : Button = view.findViewById(R.id.cancel_water_goal_button)
        cancelWaterGoalButton.setOnClickListener {
            //Move back to home fragment
            returnHome()
        }
    }

    private fun returnHome() {
        val manager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, HomeFragment())
        transaction.commit()
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