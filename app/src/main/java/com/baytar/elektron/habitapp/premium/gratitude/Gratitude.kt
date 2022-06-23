package com.baytar.elektron.habitapp.premium.gratitude

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.GratitudeBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 */
class Gratitude : AppCompatActivity() {
    private lateinit var binding: GratitudeBinding

    private lateinit var gratefulText: TextView
    private lateinit var pen: ImageView
    private lateinit var addingLayout: ViewGroup
    private lateinit var gratitudeDate: TextView
    /**
     *
     */
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GratitudeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gratefulText = binding.gratefultext
        pen = binding.pen
        addingLayout = binding.addinglayout
        gratitudeDate = binding.gratitudeDate
        val window: Window = this@Gratitude.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@Gratitude, R.color.clock)
        val sharedPreferences =
            this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        sharedPreferences.apply {
            gratefulText.text = getString("edit", "")
        }
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val currentDate = sdf.format(Date())
        gratitudeDate.text = currentDate

        val save = intent.getStringExtra("edit")
        if (save != null) {
            if (save.isNotEmpty()) {
                gratefulText.text = save
                sharedPreferences.edit().putString("edit", gratefulText.text.toString())
                    .apply()
            }
        }
        pen.setOnClickListener {
            val intent = Intent(this, GratitudeSave::class.java)
            val bundle = Bundle()
            bundle.putString("edit", gratefulText.text.toString())
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
            //////////  Adding  programatically  textviev ///////////////////////////////////
            /* val tv = TextView(this)
            tv.textSize = 15f
            tv.text = "hello"
            addinglayout.addView(tv)*/
            /////////////////////////////////////////////////////////////////////////////////
        }
        if (gratefulText.text.isNotEmpty()) {
            addingLayout.setOnClickListener {
                val intent = Intent(this, GratitudeSave::class.java)
                val bundle = Bundle()
                bundle.putString("edit", gratefulText.text.toString())
                intent.putExtras(bundle)
                startActivity(intent)
                finish()
            }
        }
    }
}
