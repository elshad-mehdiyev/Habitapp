package com.baytar.elektron.habitapp.premium.walk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.WalkBinding
import com.baytar.elektron.habitapp.premium.util.WalkActivity
import me.itangqi.waveloadingview.WaveLoadingView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow


/**
 *
 */
class Walk : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: WalkBinding

    private lateinit var simpleChronometer: Chronometer
    private lateinit var stepValue: TextView
    private lateinit var stepGoalCount: TextView
    private lateinit var setGoal: Button
    private lateinit var km: TextView
    private lateinit var kCal: TextView
    private lateinit var waveDrink: WaveLoadingView
    private lateinit var editGoalCount: EditText
    private lateinit var cancelGoalCount: Button
    private lateinit var button: Button
    private lateinit var walkGoalScreen: ViewGroup
    private lateinit var walkMainScreen: ViewGroup
    private lateinit var walkProfile: ImageView
    private lateinit var saveGoalCount: Button

    /**
     *
     */
    private var running: Boolean = false

    /**
     *
     */
    private var totalstep: Float = 0f

    /**
     *
     */
    private var previously: Float = 0f
    private var goalstate = 0

    /**
     *
     */
    private var background = 1
    private val df = DecimalFormat("#.##")
    private var stepGoal = 0

    /**
     *
     */
    private var sensorManager: SensorManager? = null
    private var sharedPreferences: SharedPreferences? = null
    private var lastPause: Long = 0
    private var startWalkTime: Long = 0
    private var difference: Long = 0
    private var kilometer = 0f
    private val integerChars: CharRange = '0'..'9'

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WalkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simpleChronometer = binding.simpleChronometer
        stepValue = binding.stepvalue
        stepGoalCount = binding.stepGoalCount
        setGoal = binding.setGoal
        km = binding.km
        kCal = binding.kcal
        waveDrink = binding.waveDrink
        editGoalCount = binding.editGoalCount
        cancelGoalCount = binding.cancelGoalCount
        button = binding.button
        walkGoalScreen = binding.walkGoalScreen
        walkMainScreen = binding.walkMainScreen
        walkProfile = binding.walkProfile
        saveGoalCount = binding.saveGoalCount

        simpleChronometer.base = SystemClock.elapsedRealtime()
        lastPause = SystemClock.elapsedRealtime()
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        sharedPreferences?.apply {
            stepValue.text = getString("stepvalue", "0")
            km.text = getString("km", "0")
            waveDrink.progressValue = getInt("progress", waveDrink.progressValue)
            stepGoalCount.text = getString("edit_goal_count", "5000")
            stepGoal = getInt("step_goal", 1)
            difference = getLong("difference", 0)
            kCal.text = getString("kcal", "0")
        }
        loadSettingWalk()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepGoal = stepGoalCount.text.toString().toInt() / 100
        goalstate = (stepGoalCount.text).toString().toInt()
        waveDrink.progressValue = stepValue.text.toString().toInt() / stepGoal
        val window: Window = this@Walk.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@Walk, R.color.prussianblue)
        if (stepValue.text.toString() == stepGoalCount.text.toString()) {
            stepValue.text = "0"
        }

        walkProfile.setOnClickListener {
            startActivity(
                Intent(this, WalkActivity::class.java)
            )
        }
        button.setOnClickListener {
            if (background == 1) {
                startWalkTime = System.currentTimeMillis()
                simpleChronometer.base = (simpleChronometer.base
                        + SystemClock.elapsedRealtime()
                        - lastPause)
                simpleChronometer.start()
                running = true
                waveDrink.progressValue = stepValue.text.toString().toInt() / stepGoal
                button.setBackgroundResource(R.drawable.pause)
                button.text = "Pause"
                background = 2
            } else if (background == 2) {
                difference = (System.currentTimeMillis() - startWalkTime) / 1000
                lastPause = SystemClock.elapsedRealtime()
                simpleChronometer.stop()
                running = false
                waveDrink.progressValue = stepValue.text.toString().toInt() / stepGoal
                button.setBackgroundResource(R.drawable.resume)
                button.text = "Resume"
                background = 1
                if (difference > 60) {
                    sharedPreferences?.edit()
                        ?.putLong("difference", difference)
                        ?.apply()
                }
            }
        }
        setGoal.setOnClickListener {
            walkMainScreen.visibility = View.GONE
            walkGoalScreen.visibility = View.VISIBLE
        }
        saveGoalCount.setOnClickListener {
            if (editGoalCount.text.isNotEmpty()) {
                if (editGoalCount.text.toString().toInt() > stepValue.text.toString().toInt()
                    && editGoalCount.text.isNotEmpty()
                ) {
                    stepGoalCount.text = editGoalCount.text.toString()
                    stepGoal = stepGoalCount.text.toString().toInt() / 100
                } else {
                    stepGoalCount.text = "5000"
                }
            }
            sharedPreferences?.edit()
                ?.putString("edit_goal_count", editGoalCount.text.toString())
                ?.putInt("step_goal", stepGoal)
                ?.apply()
            walkMainScreen.visibility = View.VISIBLE
            walkGoalScreen.visibility = View.GONE
        }
        cancelGoalCount.setOnClickListener {
            walkMainScreen.visibility = View.VISIBLE
            walkGoalScreen.visibility = View.GONE
        }

    }

    /**
     *
     */
    override fun onResume() {
        super.onResume()
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepsSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(
                this,
                stepsSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    /**
     *
     */
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        if (running) {
            totalstep = event.values[0]
            val currentstep = totalstep.toInt() - previously.toInt()
            stepValue.text = ("$currentstep")

            stepGoal = stepGoalCount.text.toString().toInt() / 100
            waveDrink.progressValue = currentstep / stepGoal
            kilometer = ((0.762 * currentstep) / 1000).toFloat()
            df.roundingMode = RoundingMode.CEILING
            km.text = df.format(kilometer)
            if (totalstep == goalstate.toFloat()) {
                stepValue.text = "0"
            }
            sharedPreferences?.edit()
                ?.putString("stepvalue", stepValue.text.toString())
                ?.putString("km", km.text.toString())
                ?.putInt("progress", waveDrink.progressValue)
                ?.putInt("step_goal", stepGoal)
                ?.apply()
        }
    }

    private fun isInteger(input: String): Boolean = input.all { it in integerChars }
    private fun loadSettingWalk() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val weightUnit = sharedPref.getString("kg_unit", "")
        val weight = sharedPref.getString("weight_walk", "")
        val heightUnit = sharedPref.getString("m_unit", "")
        val height = sharedPref.getString("Height_walk", "")
        val cf = DecimalFormat("#.#")
        if (weightUnit == "kg" && heightUnit == "cm" && weight?.let { isInteger(it) } == true && weight.isNotEmpty()
            && height?.let { isInteger(it) } == true && height.isNotEmpty()) {
            val kg = weight.toString().toInt()
            val m = height.toString().toInt()
            val velocity = (kilometer * 1000) / difference
            val caloriePerMinute =
                (0.035 * kg + (velocity.pow(2) / m) * 0.029 * kg) * (difference / 60)
            kCal.text = cf.format(caloriePerMinute)
            sharedPreferences?.edit()
                ?.putString("kcal", kCal.text.toString())
                ?.apply()
        } else if (weightUnit == "kg" && heightUnit == "in" && weight?.let { isInteger(it) } == true && weight.isNotEmpty()
            && height?.let { isInteger(it) } == true && height.isNotEmpty()) {
            val kg = weight.toString().toInt()
            val m = height.toString().toInt() * 2.54
            val velocity = (kilometer * 1000) / difference
            val caloriePerMinute =
                0.035 * kg + (velocity.pow(2) / m) * 0.029 * kg * (difference / 60)
            kCal.text = cf.format(caloriePerMinute)
            sharedPreferences?.edit()
                ?.putString("kcal", kCal.text.toString())
                ?.apply()

        } else if (weightUnit == "lb" && heightUnit == "cm" && weight?.let { isInteger(it) } == true && weight.isNotEmpty()
            && height?.let { isInteger(it) } == true && height.isNotEmpty()) {
            val kg = weight.toString().toInt() * 0.453
            val m = height.toString().toInt()
            val velocity = (kilometer * 1000) / difference
            val caloriePerMinute =
                0.035 * kg + (velocity.pow(2) / m) * 0.029 * kg * (difference / 60)
            kCal.text = cf.format(caloriePerMinute)
            sharedPreferences?.edit()
                ?.putString("kcal", kCal.text.toString())
                ?.apply()

        } else if (weightUnit == "lb" && heightUnit == "in" && weight?.let { isInteger(it) } == true && weight.isNotEmpty()
            && height?.let { isInteger(it) } == true && height.isNotEmpty()) {
            val kg = weight.toString().toInt() * 0.453
            val m = height.toString().toInt() * 2.54
            val velocity = (kilometer * 1000) / difference
            val caloriePerMinute =
                0.035 * kg + (velocity.pow(2) / m) * 0.029 * kg * (difference / 60)
            kCal.text = cf.format(caloriePerMinute)
            sharedPreferences?.edit()
                ?.putString("kcal", kCal.text.toString())
                ?.apply()

        }
    }
}
