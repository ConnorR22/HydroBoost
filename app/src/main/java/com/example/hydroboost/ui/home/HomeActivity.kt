package com.example.hydroboost.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hydroboost.R
import com.example.hydroboost.databinding.ActivityHomeBinding
import com.example.hydroboost.ui.history.HistoryFragment
import com.example.hydroboost.ui.metrics.MetricsFragment
import com.example.hydroboost.ui.notifications.RemindersFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}