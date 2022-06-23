package com.baytar.elektron.habitapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.databinding.ActivityMainBinding
import com.baytar.elektron.habitapp.premium.*
import com.baytar.elektron.habitapp.premium.gratitude.Gratitude
import com.baytar.elektron.habitapp.premium.walk.Walk
import com.google.android.material.navigation.NavigationBarView


/**
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    /**
     *
     */
    @SuppressLint("SetTextI18n", "LogConditional", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout XML file and return a binding object instance
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.root)
        val habitText1 = binding.habitText1
        val meditationIcon = binding.meditationIcon
        val gratitudeIcon = binding.gratitudeIcon
        val callIcon = binding.callIcon
        val walkIcon = binding.walkIcon
        val motivationIcon = binding.motivationIcon
        val drinkIcon = binding.drinkIcon
        val pomodoroIcon = binding.pomodoroIcon

        val sharedPreferences =
            this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        sharedPreferences.apply {
            habitText1?.text   = getString("habit_name", "")
        }
        meditationIcon.setOnClickListener {
            startActivity(
                Intent(this, Silence::class.java)
            )
        }
        gratitudeIcon.setOnClickListener {
            startActivity(
                Intent(this, Gratitude::class.java)
            )
        }
        callIcon.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            )
        }
        walkIcon.setOnClickListener {
            startActivity(
                Intent(this, Walk::class.java)
            )
        }
        motivationIcon.setOnClickListener {
            startActivity(
                Intent(this, StayMotivated::class.java)
            )
        }
        drinkIcon.setOnClickListener {
            startActivity(
                Intent(this, Drink::class.java)
            )
        }
        pomodoroIcon.setOnClickListener {
            startActivity(
                Intent(this, Pomodoro::class.java)
            )
        }
        binding.addHabit?.setOnClickListener {
            startActivity(
                Intent(this, AddHabit::class.java)
            )
        }
        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item1 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.item2 -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.item3 -> {
                    // Respond to navigation item 2 click
                    true
                }

                else -> false
            }
        }

        val window: Window = this@MainActivity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.sea)


        ///////////////////////////////////////////////////////////////////////////////////////////  ADS  finish //////////////////////
        val save = intent.getStringExtra("habit_name")
        if (save != null) {
            if (save.isNotEmpty()) {
                habitText1?.text = save
                sharedPreferences.edit().putString("habit_name", habitText1?.text.toString())
                    .apply()
            }
        }
        /////////////////////////////////////////////////// DARK  MODE //////////////////////////////
        val dark: Boolean = sharedPreferences.getBoolean("NightMode", false)

        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.settingMain?.setOnClickListener {
            if (dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("NightMode", false)
                    .apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("NightMode", true)
                    .apply()
            }
        }
    }
}


