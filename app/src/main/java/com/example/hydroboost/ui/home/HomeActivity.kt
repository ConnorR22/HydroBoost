package com.example.hydroboost.ui.home

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hydroboost.R

class HomeActivity : AppCompatActivity() {
    private val CHANNEL_ID = "notification_channel"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        createNotificationChannel()

        // TODO must DELETE when navigation bar is fully implemented
        val temp = findViewById<Button>(R.id.button)
        temp.setText("Notification Settings")
        temp.setOnClickListener {
            val i = Intent(this@HomeActivity, NotificationActivity::class.java)
            startActivity(i)
        }

        // TODO must DELETE when custom reminders are fully implemented
        val temp2 = findViewById<Button>(R.id.button2)
        temp2.setText("Send Notification")
        temp2.setOnClickListener {

            val intent = Intent(applicationContext, NotificationBroadcast::class.java)
            val pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)
        }

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