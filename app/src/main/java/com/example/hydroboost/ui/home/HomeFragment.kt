package com.example.hydroboost.ui.home

/**
 * A fragment representing the home screen of HydroBoost.
 * Displays a water bottle animation representing daily water intake.
 * Also displays hydration facts, and a link to the LogWaterFragment to log water entries
 * and set daily water goal.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.hydroboost.R
import com.example.hydroboost.data.SharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var waterFillingView : View? = null
    private var sharedPreferences : SharedPreferences? = null
    private lateinit var waterBottleImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView as ViewGroup

        //Instantiate a SharedPreferences Object
        sharedPreferences = SharedPreferences(requireContext())

        //Add the water bottle image to the view
        addWaterBottle(rootView)

        //A listener to the logWaterButton that switches to the LogWaterFragment
        val logWaterButton : Button = rootView.findViewById(R.id.log_water_button)
        logWaterButton.setOnClickListener {
            goToLogWater()
        }

        return rootView
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as ViewGroup

        //Fill the water bottle with water
        fillWaterBottle(view)

        //Display and iterate through hydration messages
        inspirationalMessage(view)
    }

    /**
     * A function used to handle filling the water bottle image with water, as an animation.
     * @param view: The current View.
     */
    private fun fillWaterBottle(view : View) {
        view as ViewGroup

        //Define percentage of water goal met
        val percentage = sharedPreferences?.getPercentageOfGoalDrank() //percentage = 1: 1%, percentage = 100: 100%
        //Fill the water bottle within the bottle image based on percentage
        lifecycleScope.launch {
            for (i in 1..percentage!!) {
                removeWater(view)
                removeWaterBottle(view)

                if (percentage != null)
                    addWater(
                        view,
                        371,
                        (1413 - (1068 * (i / 100.0))).toInt(),
                        338,
                        (1068 * (i / 100.0)).toInt()
                    )
                addWaterBottle(view)
                delay(15)
            }
        }
    }

    /**
     * A function used to add the water bottle image to the view.
     * @param view: The current View.
     */
    private fun addWaterBottle(view : View) {
        view as ViewGroup
        waterBottleImage = ImageView(requireContext())

        //Get percentage of water goal met
        val percentage = sharedPreferences?.getPercentageOfGoalDrank()

        //If water goal met, display golden water bottle, else normal water bottle
        if (percentage == 100) {
            waterBottleImage.setImageResource(R.drawable.golden_water_bottle_with_background)
            val dailyGoalPercentageView : TextView = view?.findViewById(R.id.daily_goal_percentage) as TextView
            dailyGoalPercentageView.setTextColor(requireContext().getColor(R.color.color7))
        } else
            waterBottleImage.setImageResource(R.drawable.water_bottle_with_background)

        val waterBottleImageParams = LinearLayout.LayoutParams(338, 1284)
        waterBottleImageParams.setMargins(371, 150, 0, 0)
        waterBottleImage.layoutParams = waterBottleImageParams
        waterBottleImage.id = R.id.waterBottleImage
        view.addView(waterBottleImage)
    }

    /**
     * A function used to remove the water bottle from the view.
     * @param view: The current View.
     */
    private fun removeWaterBottle(view : View) {
        view as ViewGroup
        view.removeView(waterBottleImage)
    }

    /**
     * A function used to draw each frame of the water bottle filling with water.
     * @param rootView: The current view.
     * @param x: x position to draw each rectangle frame.
     * @param y: y position to draw each rectangle frame.
     * @param rectangleWidth: The width of each rectangle frame.
     * @param rectangleHeight: The height of each rectangle frame.
     */
    private fun addWater(rootView: View, x : Int, y : Int, rectangleWidth : Int, rectangleHeight : Int) {
        rootView as ViewGroup
        waterFillingView = WaterFillingView(requireContext(), null, x, y, rectangleWidth, rectangleHeight)
        rootView.addView(waterFillingView)
        displayDailyGoalPercentage(rootView)
    }

    /**
     * A function used to remove the current water bottle frame (at the end of the filling
     * animation.)
     * @param view: The current View.
     */
    private fun removeWater(view : View) {
        view as ViewGroup
        view.removeView(waterFillingView)
    }

    /**
     * A function used to display the goal percentage TextView within the water bottle image.
     * Calculated based off of a percentage of daily logged water drank to WATER_GOAL.
     * @param view: The current View.
     */
    private fun displayDailyGoalPercentage(view : View) {
        view as ViewGroup
        val dailyGoalPercentageView : TextView = view?.findViewById(R.id.daily_goal_percentage) as TextView
        view.removeView(dailyGoalPercentageView)

        val percentage = sharedPreferences?.getPercentageOfGoalDrank().toString() + "%"
        dailyGoalPercentageView.text = percentage

        view.addView(dailyGoalPercentageView)
    }

    /**
     * A function used to go to the LogWaterFragment.
     */
    private fun goToLogWater() {
        val manager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, LogWaterFragment())
        transaction.commit()
    }

    /**
     * A function used to display and iterate through inspirational messages at the bottom
     * underneath the water bottle image.
     * @param view: The current View.
     */
    private fun inspirationalMessage(view : View) {
        //Define array of hydration messages based off IDs
        val hydration_message_ids : Array<Int> = arrayOf(
            R.string.hydration_message_1,
            R.string.hydration_message_2,
            R.string.hydration_message_3,
            R.string.hydration_message_4,
            R.string.hydration_message_5,
        )

        var count = 0
        //Iterate through hydration messages on loop, every 10 seconds
        lifecycleScope.launch {
            while (true) {
                val inspirationalMessage: TextView =
                    view.findViewById(R.id.hydration_message_text_view) as TextView

                inspirationalMessage.text = getString(hydration_message_ids[count])

                count++
                if (count > 4)
                    count = 0
                delay(10000)
            }
        }
    }
}