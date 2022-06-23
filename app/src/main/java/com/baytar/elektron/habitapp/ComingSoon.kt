package com.baytar.elektron.habitapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.databinding.ComingSoonBinding

/**
 *
 */
class ComingSoon : AppCompatActivity() {
    private lateinit var binding: ComingSoonBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ComingSoonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this@ComingSoon.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@ComingSoon, R.color.colorPrimary)
        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}