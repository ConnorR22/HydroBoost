package com.example.hydroboost.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hydroboost.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // TODO must delete when navigation bar is fully implemented
        val temp = findViewById<Button>(R.id.button)
        temp.setOnClickListener {
            val i = Intent(this@HomeActivity, NotificationActivity::class.java)
            startActivity(i)
        }
    }
}