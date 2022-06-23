package com.baytar.elektron.habitapp


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.databinding.AddHabitBinding
import com.google.android.gms.ads.*


/**
 *
 */
class AddHabit : AppCompatActivity() {
    private lateinit var binding: AddHabitBinding
    /**
     *
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddHabitBinding.inflate(layoutInflater)
        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.root)

        val timesAddHabit = binding.timesAddHabit
        val habitName =  binding.habitName

        var times = timesAddHabit.text.toString().toInt()
        val window: Window = this@AddHabit.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@AddHabit, R.color.gainsboro)
        binding.backAddHabit.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
        binding.createAdd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("habit_name", habitName.text.toString())
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
        binding.addAddhabit.setOnClickListener {
            times++
            timesAddHabit.text = times.toString()
        }
        binding.minusAddhabit.setOnClickListener {
            times--
            if (times < 1) {
                times = 1
            }
            timesAddHabit.text = times.toString()
        }


    }
}
