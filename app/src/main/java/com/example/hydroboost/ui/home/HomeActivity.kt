package com.example.hydroboost.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
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

    private fun updateFrameLayoutFromNavBar(fragment : Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "HydroBoost Notifications"
            val descriptionText = "Hydration reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = descriptionText
            // Register the channel with the system
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}