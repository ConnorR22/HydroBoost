package com.example.hydroboost.ui.history

/**
 * A fragment representing water log history.
 * Water logs are grouped together by date, can be removed from this fragment.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.hydroboost.R
import com.example.hydroboost.data.SharedPreferences

class HistoryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as ViewGroup

        //Create SharedPreferences instance
        val sharedPreferences = SharedPreferences(requireContext())

        //Get a list of dates in descending order
        val sharedPreferencesListSorted = sharedPreferences.getDescendingDates()
        val historyListLinearLayout = view?.findViewById<LinearLayout>(R.id.history_list)

        //Create a MutableList to hold unique date values
        val uniqueDates : MutableList<String> = mutableListOf<String>()
        for ((dateTime, amount) in sharedPreferencesListSorted) {
            //Skip over WATER_GOAL instance in SharedPreferences
            if (dateTime == "WATER_GOAL")
                continue

            //Separate date, time values
            val date = dateTime.substring(0, 10)
            val time = dateTime.substring(11, 19)

            //Create a header for each unique date and add it to View
            if (! uniqueDates.contains(date)) {
                uniqueDates.add(date)
                val dateHeader = dateHeaderFun(date)
                historyListLinearLayout?.addView(dateHeader)

                val headerLine = headerLineFun()
                historyListLinearLayout?.addView(headerLine)
            }

            //Instantiate the water log entry, and removal button to View
            val waterLogButtonContainer = waterLogButtonContainerFun(dateTime)
            val waterLogEntry = waterLogEntryFun(date, time, amount as Int)
            val removeLogButton = removeLogButtonFun(dateTime)

            //A listener to remove water log entry when remove button pressed.
            removeLogButton.setOnClickListener() {
                sharedPreferences.remove(dateTime)
                returnHistory()
            }

            //Add the water log entry, and removal button to View
            waterLogButtonContainer?.addView(waterLogEntry)
            waterLogButtonContainer?.addView(removeLogButton)
            historyListLinearLayout?.addView(waterLogButtonContainer)
        }
    }

    /**
     * A function to return to (reload) the HistoryFragment.
     */
    private fun returnHistory() {
        val manager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, HistoryFragment())
        transaction.commit()
    }

    /**
     * A function used to create a TextView as a date header within the LinearLayout, within the
     * ScrollView of this Fragment.
     * @param date: A String representing each unique date (yyyy-mm-dd).
     * @return The created TextView.
     */
    private fun dateHeaderFun(date : String): TextView {
        val dateHeader = TextView(requireContext())
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        layoutParams.setMargins(25, 25,0, 0)
        dateHeader.layoutParams = layoutParams

        dateHeader.text = date
        dateHeader.textSize = 28F
        dateHeader.setTextColor(requireContext().getColor(R.color.black))

        val typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        dateHeader.typeface = typeface

        return dateHeader
    }

    /**
     * A function used to create a View to separate the dateHeader and the water logs within the
     * LinearLayout, within the ScrollView of this Fragment.
     * @param date: A String representing each unique date (yyyy-mm-dd).
     * @return The created View.
     */
    private fun headerLineFun() : View {
        val headerLine = View(requireContext())
        val layoutParams = LinearLayout.LayoutParams(1060, 6)

        layoutParams.setMargins(10, 0, 0, 0)
        headerLine.layoutParams = layoutParams

        headerLine.setBackgroundColor(requireContext().getColor(R.color.black))

        return headerLine
    }

    /**
     * A function used to create a Button to remove a log from history within the LinearLayout,
     * within the ScrollView of this Fragment.
     * @param date: A String representing each unique date (yyyy-mm-dd).
     * @return The created Button.
     */
    private fun removeLogButtonFun(dateTime : String): Button {
        val removeLogButton = Button(requireContext())
        val layoutParams = LinearLayout.LayoutParams(110, 110, 0f)
        removeLogButton.layoutParams = layoutParams

        removeLogButton.text = "X"
        removeLogButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.color3))
        removeLogButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.color6))
        removeLogButton.id = (dateTime + "_button").hashCode()

        return removeLogButton
    }

    /**
     * A function used to create a TextView representing a water logs within the
     * LinearLayout, within the ScrollView of this Fragment. Contains date, time and amount drank.
     * @param date: A String representing each date (yyyy-mm-dd).
     * @param time: A String representing the time (hh-MM-ss).
     * @param amount: An Int representing the amount of water drank for the instance.
     * @return The created TextView.
     */
    private fun waterLogEntryFun(date : String, time : String, amount : Int): TextView {
        val waterLogEntry = TextView(requireContext())
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        layoutParams.setMargins(10, 0, 0, 0)
        waterLogEntry.layoutParams = layoutParams

        waterLogEntry.text = "$date $time: $amount" + "ml"
        waterLogEntry.textSize = 20F
        waterLogEntry.setTextColor(requireContext().getColor(R.color.black))

        val typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
        waterLogEntry.typeface = typeface

        return waterLogEntry
    }

    /**
     * A function used to create a LinearLayout to hold the water log entry and remove button within
     * the LinearLayout, within the ScrollView of this Fragment.
     * @param date: A String representing each date (yyyy-mm-dd).
     * @return The created LinearLayout.
     */
    private fun waterLogButtonContainerFun(dateTime : String): LinearLayout {
        val waterLogButtonContainer = LinearLayout(requireContext())
        waterLogButtonContainer.orientation = LinearLayout.HORIZONTAL
        waterLogButtonContainer.gravity = Gravity.HORIZONTAL_GRAVITY_MASK

        waterLogButtonContainer.id = (dateTime + "_container").hashCode()

        return waterLogButtonContainer
    }
}