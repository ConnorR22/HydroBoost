package com.example.hydroboost.ui.home

/**
 * An activity used to host the navigation bar to switch between fragments.
 * Navigates between HomeFragment, NotificationsFragment, MetricsFragment and HistoryFragment.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hydroboost.R
import com.example.hydroboost.databinding.ActivityHomeBinding
import com.example.hydroboost.ui.history.HistoryFragment
import com.example.hydroboost.ui.metrics.MetricsFragment
import com.example.hydroboost.ui.notifications.RemindersFragment

class HomeActivity : AppCompatActivity() {

    private val CHANNEL_ID = "notification_channel"

    private lateinit var homeBinding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        //Handle nav bar selection events.
        homeBinding.navBar.setOnItemSelectedListener {
            var item = it.itemId

            if (item == R.id.homeIcon)
                updateFrameLayoutFromNavBar(HomeFragment())
            else if (item == R.id.notificationsIcon)
                updateFrameLayoutFromNavBar(RemindersFragment())
            else if (item == R.id.metricsIcon)
                updateFrameLayoutFromNavBar(MetricsFragment())
            else if (item == R.id.historyIcon)
                updateFrameLayoutFromNavBar(HistoryFragment())

            true
        }

        updateFrameLayoutFromNavBar(HomeFragment())
    }

    /**
     * A function used to switch fragments as defined from nav bar selection.
     * @param fragment: The fragment to switch to.
     */
    private fun updateFrameLayoutFromNavBar(fragment : Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "HydroBoost Notifications"
            val descriptionText = "Hydration reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}