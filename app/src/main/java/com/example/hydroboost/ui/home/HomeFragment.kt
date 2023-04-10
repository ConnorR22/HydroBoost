package com.example.hydroboost.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.example.hydroboost.R
import com.example.hydroboost.ui.SharedPreferences
import com.example.hydroboost.ui.notifications.NotificationBroadcast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var waterFillingView : View? = null
    private var sharedPreferences : SharedPreferences? = null
    private lateinit var waterBottleImage : ImageView
    private var activeReminderToSet : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView as ViewGroup //This FrameLayout should be changed to a ViewGroup to avoid explicit casting

        sharedPreferences = SharedPreferences(requireContext())

        addWaterBottle(rootView)

        val logWaterButton : Button = rootView.findViewById(R.id.log_water_button)
        logWaterButton.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, LogWaterFragment())
            transaction.commit()
        }

        val settingsButton = rootView.findViewById<ImageButton>(R.id.settingsButton)

        startReminders()
        cancelReminders()

        return rootView
    }

    private fun startReminders() {
        val intent = Intent(context, NotificationBroadcast::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val sharedPreferencesSettings = context?.getSharedPreferences(
            context!!.getString(R.string.reminder_settings),
            Context.MODE_PRIVATE
        )
        val customReminderPreferences = context?.getSharedPreferences(
            context!!.getString(R.string.custom_reminders_settings),
            Context.MODE_PRIVATE
        )
        var customReminders = customReminderPreferences?.getString("CUSTOM_REMINDERS", "")
        var activeReminders = customReminderPreferences?.getString("ACTIVE_REMINDERS", "")

        activeReminderToSet = ""

        var calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)

        var crs = customReminders?.split("|")
        var crsActive = activeReminders?.split("|")
        for (reminder in crs!!){
            if (reminder != ""){
                for (active in crsActive!!) {

                    var fields = reminder.split(",")
                    var activeDay = 0
                    when (fields[4]){
                        "Sunday" -> activeDay = 1
                        "Monday" -> activeDay = 2
                        "Tuesday" -> activeDay = 3
                        "Wednesday" -> activeDay = 4
                        "Thursday" -> activeDay = 5
                        "Friday" -> activeDay = 6
                        "Saturday" -> activeDay = 7
                    }

                    if (fields[0].equals(active) && activeDay == day)
                        activeReminderToSet = reminder
                        break
                }
            }
        }

        var style = ""
        var startTime = ""

        if (activeReminderToSet != ""){
            style = activeReminderToSet!!.split(",")[1]
            startTime = activeReminderToSet!!.split(",")[2]
        } else {
            style = sharedPreferencesSettings!!.getString("style", "").toString()
            startTime = sharedPreferencesSettings.getString("startTime", "").toString()
        }

        var interval = 0L
        when (style) {
            "Every 30 Minutes" -> interval = AlarmManager.INTERVAL_HALF_HOUR
            "Every Hour" -> interval = AlarmManager.INTERVAL_HOUR
            "Every 2 Hours" -> interval = AlarmManager.INTERVAL_HOUR * 2
        }

        val times1 = startTime.split(":")
        calendar[Calendar.HOUR_OF_DAY] = times1.get(0).toInt()
        calendar[Calendar.MINUTE] = times1.get(1).toInt()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            interval,
            pendingIntent
        )
    }

    private fun cancelReminders() {
        val intent = Intent(context, NotificationBroadcast::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val sharedPreferencesSettings = context?.getSharedPreferences(
            context!!.getString(R.string.reminder_settings),
            Context.MODE_PRIVATE
        )

        var endTime = ""
        if (activeReminderToSet != ""){
            endTime = activeReminderToSet!!.split(",")[2]
        } else {
            endTime = sharedPreferencesSettings!!.getString("endTime", "").toString()
        }

        val times1 = endTime.split(":")
        var calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = times1!!.get(0).toInt()
        calendar[Calendar.MINUTE] = times1.get(1).toInt()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        if (calendar.timeInMillis < System.currentTimeMillis()){
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as ViewGroup

//        lifecycleScope.launch {
//            for (i in 1..9) {
//                fillWaterBottle(view, i)
//                delay(1000)
//            }
//            removeWater(view)
//        }

        fillWaterBottle(view)

        val hydration_message_ids : Array<Int> = arrayOf(
            R.string.hydration_message_1,
            R.string.hydration_message_2,
            R.string.hydration_message_3,
            R.string.hydration_message_4,
            R.string.hydration_message_5,
        )
        var count = 0
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

    fun fillWaterBottle(view : View) {
        view as ViewGroup
//        lifecycleScope.launch {
//            for (i in 1..9) {
//                fillWaterBottle(view, i)
//                delay(1000)
//            }
//            removeWater(view)
//        }
        val percentage = sharedPreferences?.getPercentageOfGoalDrank() //percentage = 1: 1%, percentage = 100: 100%

        lifecycleScope.launch {
            for (i in 1..percentage!!) {
                view.removeView(waterFillingView) //Remove the water
                view.removeView(waterBottleImage) //Remove the bottle

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

    fun addWaterBottle(view : View) {
        view as ViewGroup
        waterBottleImage = ImageView(requireContext())

        waterBottleImage.setImageResource(R.drawable.water_bottle_with_background)
        val waterBottleImageParams = LinearLayout.LayoutParams(338, 1284)
        waterBottleImageParams.setMargins(371, 150, 0, 0)
        waterBottleImage.layoutParams = waterBottleImageParams
        waterBottleImage.id = R.id.waterBottleImage
        view.addView(waterBottleImage)
    }

    fun removeWaterBottle(view : View) {
        view as ViewGroup
        view.removeView(waterBottleImage)
    }

    fun addWater(rootView: View, x : Int, y : Int, rectangleWidth : Int, rectangleHeight : Int) {
        rootView as ViewGroup
        waterFillingView = WaterFillingView(requireContext(), null, x, y, rectangleWidth, rectangleHeight)
        rootView.addView(waterFillingView)
    }

    fun removeWater(view : View) {
        view as ViewGroup
        view.removeView(waterFillingView)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}