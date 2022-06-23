package com.baytar.elektron.habitapp.premium.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baytar.elektron.habitapp.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
    }
}