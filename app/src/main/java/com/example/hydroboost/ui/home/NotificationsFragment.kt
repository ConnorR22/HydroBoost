package com.example.hydroboost.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.hydroboost.R
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val DEFAULT_NOTIFICATION_MESSAGE = "Time to hydrate!"

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotificationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.context?.getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        val saveButton = view.findViewById<Button>(R.id.saveNotifications)
        val messageEdit = view.findViewById<TextView>(R.id.messageEdit)
        val deliverySwitch = view.findViewById<Switch>(R.id.deliverySwitch)
        val remindersSwitch = view.findViewById<Switch>(R.id.remindersSwitch)

        if (preferences != null) {
            messageEdit.setText(preferences.getString("message", DEFAULT_NOTIFICATION_MESSAGE))
            deliverySwitch.setChecked(preferences.getBoolean("delivery", false))
            remindersSwitch.setChecked(preferences.getBoolean("reminder", false))
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            // "it" refers to whether the notification permissions were enabled - let the user know the status upon making their choice
            if (!it) {
                view.findViewById<View>(android.R.id.content)?.rootView?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Please permit notifications in the App Settings",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                view.findViewById<View>(android.R.id.content)?.rootView?.let { it1 ->
                    Snackbar.make(
                        it1,
                        "Notifications are now enabled!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        backButton.setOnClickListener {
            val i = Intent(context, HomeActivity::class.java)
            startActivity(i)
        }

        saveButton.setOnClickListener {
            if (preferences != null) {
                saveNotification(preferences)
            }
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun saveNotification(preferences: SharedPreferences) {
        val messageEdit = view?.findViewById<EditText>(R.id.messageEdit)
        val messageText = messageEdit?.text.toString()

        val deliverySwitch = view?.findViewById<Switch>(R.id.deliverySwitch)

        var editor = preferences.edit()
        editor.putString("message", messageText)
        deliverySwitch?.isChecked()?.let { editor.putBoolean("delivery", it) }
        editor.apply()

        // give the user the opportunity to allow notifications from the app
        with(context?.let { NotificationManagerCompat.from(it) }) {
            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.POST_NOTIFICATIONS,
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val toast = Toast.makeText(context, "Notification settings successfully saved!", Toast.LENGTH_SHORT)
        toast.show()
    }
}