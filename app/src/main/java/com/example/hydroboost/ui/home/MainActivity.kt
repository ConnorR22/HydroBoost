package com.example.hydroboost.ui.home

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

        Handler(Looper.getMainLooper()).postDelayed({
            val switchToHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(switchToHome)
            finish()
        }, 1500)
    }
}