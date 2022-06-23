package com.baytar.elektron.habitapp.premium

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.DrinkWaterBinding
import com.baytar.elektron.habitapp.fragments.HomeFragment
import com.baytar.elektron.habitapp.fragments.LearnFragment
import com.baytar.elektron.habitapp.fragments.SettingFragment

/**
 *
 */
class Drink : AppCompatActivity() {
    private lateinit var binding: DrinkWaterBinding

    private val homeFragment = HomeFragment()
    private val learnFragment = LearnFragment()
    private val settingFragment = SettingFragment()


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkWaterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(homeFragment)
        val window: Window = this@Drink.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@Drink, R.color.sea)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.drink_home -> {
                    replaceFragment(homeFragment)
                    window.statusBarColor = ContextCompat.getColor(this@Drink, R.color.sea)
                }
                R.id.drink_history -> {
                    replaceFragment(learnFragment)
                    window.statusBarColor = ContextCompat.getColor(this@Drink, R.color.midnightblue)
                }
                R.id.drink_settings -> {
                    replaceFragment(settingFragment)
                    window.statusBarColor = ContextCompat.getColor(
                        this@Drink, R.color.gainsboro
                    )
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}