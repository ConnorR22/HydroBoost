package com.example.hydroboost.ui.home

/**
 * An activity that displays the loading screen, and transitions to the HomeFragment.
 * @author: Ben Hickman
 * @date: 2023/04/09
 * @version: 1.0.0
 */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.hydroboost.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() //Remove the action bar for loading screen

        //Define the loading screen
        Handler(Looper.getMainLooper()).postDelayed({
            val switchToHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(switchToHome)
            finish()
        }, 1500)
    }
}