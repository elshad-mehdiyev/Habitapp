package com.baytar.elektron.habitapp.premium

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.StayMotivatedBinding

/**
 *
 */
class StayMotivated : AppCompatActivity() {
    private lateinit var binding: StayMotivatedBinding
    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StayMotivatedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this@StayMotivated.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@StayMotivated, R.color.gainsboro)
        val text = listOf(
            getString(R.string.quota1),
            getString(R.string.quota2),
            getString(R.string.quota3),
            getString(R.string.quota4),
            getString(R.string.quota5),
            getString(R.string.quota6),
            getString(R.string.quota7),
            getString(R.string.quota8),
            getString(R.string.quota9),
            getString(R.string.quota10),
            getString(R.string.quota11)
        )
        val adapter = ViewPagerAdapter(text.shuffled())
        binding.viewpager.adapter = adapter
    }
}