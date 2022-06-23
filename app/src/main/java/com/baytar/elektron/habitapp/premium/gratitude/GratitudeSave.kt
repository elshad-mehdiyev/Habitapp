package com.baytar.elektron.habitapp.premium.gratitude

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.GratitudeSaveBinding

/**
 *
 */
class GratitudeSave : AppCompatActivity() {
    private lateinit var binding: GratitudeSaveBinding

    private lateinit var editSave: EditText
    private lateinit var save: ImageView
    private lateinit var delete: ImageView

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GratitudeSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editSave = binding.editsave
        save = binding.save
        delete = binding.delete
        val window: Window = this@GratitudeSave.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@GratitudeSave, R.color.midnightblue)
        val sharedPreferences =
            this.getSharedPreferences(packageName, Context.MODE_PRIVATE)

        val grate = intent.getStringExtra("edit")

        if (grate != null) {
            if (grate.isNotEmpty()) {
                editSave.setText(grate)
                sharedPreferences.edit().putString("edittext", editSave.text.toString())
                    .apply()
            }
        }

        save.setOnClickListener {
            val intent = Intent(this, Gratitude::class.java)
            val bundle = Bundle()
            bundle.putString("edit", editSave.text.toString())
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
        delete.setOnClickListener {
            startActivity(
                Intent(this, Gratitude::class.java)
            )
        }

    }
}