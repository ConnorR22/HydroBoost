package com.example.hydroboost.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private val DEFAULT_NOTIFICATION_MESSAGE = "Time to hyrdate!"

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val preferences = getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val saveButton = findViewById<Button>(R.id.saveNotifications)
        val messageEdit = findViewById<TextView>(R.id.messageEdit)
        val deliverySwitch = findViewById<Switch>(R.id.deliverySwitch)
        val remindersSwitch = findViewById<Switch>(R.id.remindersSwitch)

        messageEdit.setText(preferences.getString("message", DEFAULT_NOTIFICATION_MESSAGE))
        deliverySwitch.setChecked(preferences.getBoolean("delivery", false))
        remindersSwitch.setChecked(preferences.getBoolean("reminder", false))

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

        backButton.setOnClickListener {
            val i = Intent(this@NotificationActivity, HomeActivity::class.java)
            startActivity(i)
        }

        saveButton.setOnClickListener {
            saveNotification(preferences)
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun saveNotification(preferences: SharedPreferences) {
        val messageEdit = findViewById<EditText>(R.id.messageEdit)
        val messageText = messageEdit.text.toString()

        val deliverySwitch = findViewById<Switch>(R.id.deliverySwitch)

        var editor = preferences.edit()
        editor.putString("message", messageText)
        editor.putBoolean("delivery", deliverySwitch.isChecked())
        editor.apply()

        // give the user the opportunity to allow notifications from the app
        with(NotificationManagerCompat.from(this)) {
            if (ContextCompat.checkSelfPermission(
                    this@NotificationActivity,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val toast = Toast.makeText(applicationContext, "Notification settings successfully saved!", Toast.LENGTH_SHORT)
        toast.show()
    }

}