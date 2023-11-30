package com.example.sharedcard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharedcard.repository.QueryPreferences
import com.example.sharedcard.ui.registration.RegistrationActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
        finish()
    }
}