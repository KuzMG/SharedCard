package com.example.sharedcard.ui.splash_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedcard.R
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.util.appComponent

class SplashScreenHubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        when (appComponent.accountManager.accountExists()) {
            true -> startNextActivity(NavigationDrawerActivity::class.java)
            false -> startNextActivity(StartupActivity::class.java)
        }
    }

    private fun startNextActivity(activityClass: Class<out Activity?>) {
        val i = Intent(this, activityClass)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }
}