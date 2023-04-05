package com.example.hydroboost.ui.history

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.hydroboost.R
import com.example.hydroboost.ui.SharedPreferences

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
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
//        view as ViewGroup
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as ViewGroup

        val sharedPreferences = SharedPreferences(requireContext())

        val sharedPreferencesListSorted = sharedPreferences.getDescendingDates()
        val historyListLinearLayout = view?.findViewById<LinearLayout>(R.id.history_list)

        val uniqueDates : MutableList<String> = mutableListOf<String>()

        for ((dateTime, amount) in sharedPreferencesListSorted) {
            if (dateTime == "WATER_GOAL")
                continue

            val date = dateTime.substring(0, 10)
            val time = dateTime.substring(11, 19)

            if (! uniqueDates.contains(date)) {
                uniqueDates.add(date)
                val dateHeader = dateHeaderFun(date)
                historyListLinearLayout?.addView(dateHeader)
            }

            val waterLogButtonContainer = waterLogButtonContainerFun(dateTime)
            val waterLogEntry = waterLogEntryFun(date, time, amount as Int)
            val removeLogButton = removeLogButtonFun(dateTime)

            removeLogButton.setOnClickListener() {
                sharedPreferences.remove(dateTime)
                returnHistory()
            }

            waterLogButtonContainer?.addView(waterLogEntry)
            waterLogButtonContainer?.addView(removeLogButton)
            historyListLinearLayout?.addView(waterLogButtonContainer)
        }
    }

    private fun returnHistory() {
        val manager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, HistoryFragment())
        transaction.commit()
    }

    private fun dateHeaderFun(date : String): TextView {
        val dateHeader = TextView(requireContext())
        dateHeader.text = date
        dateHeader.textSize = 24F

        return dateHeader
    }

    private fun removeLogButtonFun(dateTime : String): Button {
        val removeLogButton = Button(requireContext())
        removeLogButton.layoutParams = LinearLayout.LayoutParams(100, 100)
        removeLogButton.text = "X"
        removeLogButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.color3))
        removeLogButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.color6))
        removeLogButton.id = (dateTime + "_button").hashCode()

        return removeLogButton
    }

    private fun waterLogEntryFun(date : String, time : String, amount : Int): TextView {
        val waterLogEntry = TextView(requireContext())
        waterLogEntry.text = "$date $time: $amount" + "ml"
        waterLogEntry.textSize = 16F

        return waterLogEntry
    }

    private fun waterLogButtonContainerFun(dateTime : String): LinearLayout {
        val waterLogButtonContainer = LinearLayout(requireContext())
        waterLogButtonContainer.orientation = LinearLayout.HORIZONTAL
        waterLogButtonContainer.id = (dateTime + "_container").hashCode()

        return waterLogButtonContainer
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment history.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}