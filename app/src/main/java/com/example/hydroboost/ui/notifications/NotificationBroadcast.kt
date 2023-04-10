package com.example.hydroboost.ui.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.hydroboost.R
import com.example.hydroboost.ui.home.HomeActivity
import java.util.*

/**
 * @author Emily Tan
 * @date April 2023
 * @purpose Show notifications for when scheduled by alarm manager
 **/
class NotificationBroadcast : BroadcastReceiver() {
    private val CHANNEL_ID = "notification_channel"
    private val NOTIFICATION_TITLE = "HydroBoost Reminder!"
    private val DEFAULT_NOTIFICATION_MESSAGE = "Time to hyrdate!"

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = context.getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)
        val messageText = preferences.getString("message", DEFAULT_NOTIFICATION_MESSAGE)

        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_water_drop_24)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSilent(!preferences.getBoolean("delivery", false))

        with(NotificationManagerCompat.from(context)) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val randomNotificationId = (Date().getTime()/1000L)%Integer.MAX_VALUE
                notify(randomNotificationId.toInt(), builder.build())
            }
        }

    }

}