package com.example.hydroboost.ui.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.hydroboost.R
import com.google.android.material.snackbar.Snackbar

/**
 * @author Emily Tan
 * @date April 2023
 * @purpose Allow users to modify notifications for the app
 **/
class NotificationActivity : AppCompatActivity() {

    private val CHANNEL_ID = "notification_channel"
    private val NOTIFICATION_ID = 1
    private val NOTIFICATION_TITLE = "HydroBoost Reminder!"

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        var backButton = findViewById<ImageButton>(R.id.backButton)
        var saveButton = findViewById<Button>(R.id.saveNotifications)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // "it" refers to whether the notification permissions were enabled - let the user know the status upon making their choice
            if (!it) {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Please permit notifications in the App Settings",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Notifications are now enabled!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        createNotificationChannel()

        backButton.setOnClickListener {
            val i = Intent(this@NotificationActivity, HomeActivity::class.java)
            startActivity(i)
        }

        saveButton.setOnClickListener {
            sendNotification()
        }
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

    private fun sendNotification() {
        var messageEdit = findViewById<EditText>(R.id.messageEdit)
        var messageText = messageEdit.text.toString()

        if (messageText.isNullOrEmpty()) {
            messageText = "Time to hydrate!"
        }

        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_water_drop_24)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ContextCompat.checkSelfPermission(
                    this@NotificationActivity,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(NOTIFICATION_ID, builder.build())
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

        }
    }
}