package com.example.hydroboost.ui.metrics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hydroboost.R
import com.example.hydroboost.ui.SharedPreferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MetricsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MetricsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var sharedPreferences : SharedPreferences? = null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metrics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val percentValue = view.findViewById<TextView>(R.id.percentValue)
        val intakeValue = view.findViewById<TextView>(R.id.intakeValue)
        val lowestValue = view.findViewById<TextView>(R.id.lowestValue)
        val highestValue = view.findViewById<TextView>(R.id.highestValue)

        val percentage = sharedPreferences?.getPercentageOfGoalDrank().toString() + "%"
        val intake = sharedPreferences?.getWaterDrankToday().toString() + "/" + sharedPreferences?.getWaterGoal() + " ML"

        var lowest = 0
        val map = sharedPreferences?.getWaterLogsToday()
        map?.forEach { entry ->
            val entryValue = entry.value as Int
            if (lowest == 0) {
                lowest = entryValue
            } else if (lowest > entryValue) {
                lowest = entryValue
            }
        }

        var highest = 0
        map?.forEach { entry ->
            val entryValue = entry.value as Int
            if (highest == 0) {
                highest = entryValue
            } else if (highest < entryValue) {
                lowest = entryValue
            }
        }


        val lowestAsString = "$lowest ML"
        val highestAsString = "$highest ML"

        percentValue.text = percentage
        intakeValue.text = intake
        lowestValue.text = lowestAsString
        highestValue.text = highestAsString

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment metrics.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MetricsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}