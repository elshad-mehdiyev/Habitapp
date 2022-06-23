package com.baytar.elektron.habitapp.premium

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.PomodoroBinding

/**
 *
 */
class Pomodoro : AppCompatActivity() {
    private lateinit var binding: PomodoroBinding
    private lateinit var prefs: SharedPreferences

    private lateinit var currentRound: TextView
    private lateinit var currentGoal: TextView
    private lateinit var pomodoroRound: EditText
    private lateinit var pomodoroDailyGoals: EditText
    private lateinit var pomodoroGoalCompleted: TextView
    private lateinit var pomodoroMenu: ImageView
    private lateinit var musicPomodoro: ImageView
    private lateinit var musicPomodoroText: TextView
    private lateinit var strictMode: ImageView
    private lateinit var strictModeText: TextView
    private lateinit var musicPomodoroMenu: ViewGroup
    private lateinit var musicPomodoroConfirm: TextView
    private lateinit var roundAndGoal: ViewGroup
    private lateinit var pomodoroBack: ImageView
    private lateinit var pomodoroBackground: ViewGroup
    private lateinit var pomodoroTimer: TextView
    private lateinit var startFocus: Button
    private lateinit var pomodoroSettingScreen: ViewGroup
    private lateinit var pomodoroButtons: ViewGroup
    private lateinit var longBreak: EditText
    private lateinit var shortBreak: EditText
    private lateinit var stop: Button
    private lateinit var continuePomodoro: Button
    private lateinit var pomodoroSession: EditText
    private lateinit var definedGoal: TextView
    private lateinit var definedRound: TextView

    /**
     *
     */
    var background: Int = 1

    /**
     *
     */
    var finish: Int = 1
    private var round = 1
    private var goals = 1


    private lateinit var countdownTimer: CountDownTimer
    private var timeInMilliSeconds = 0L


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PomodoroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentRound = binding.currentRound
        currentGoal = binding.currentGoal
        pomodoroRound = binding.pomodoroRound
        pomodoroDailyGoals = binding.pomodoroDailyGoals
        pomodoroGoalCompleted = binding.pomodoroGoalCompleted
        pomodoroMenu = binding.pomodoroMenu
        musicPomodoro = binding.musicPomodoro
        musicPomodoroText = binding.musicPomodoroText
        strictMode = binding.strictModePomodoro
        strictModeText = binding.strictModePomodoroText
        musicPomodoroMenu = binding.musicPomodoroMenu
        musicPomodoroConfirm = binding.musicPomodoroConfirm
        roundAndGoal = binding.roundAndGoal
        pomodoroBack = binding.pomodoroBack
        pomodoroBackground = binding.pomodoroBackground
        pomodoroTimer = binding.pomodoroTimer
        startFocus = binding.startFocus
        pomodoroSettingScreen = binding.pomodoroSettingScreen
        pomodoroButtons = binding.pomodoroButtons
        longBreak = binding.pomodoroLongBreak
        shortBreak = binding.pomodoroShortBreak
        stop = binding.stopPomodoro
        continuePomodoro = binding.continuePomodoro
        pomodoroSession = binding.pomodoroSession
        definedGoal = binding.definedGoal
        definedRound = binding.definedRound

        val window: Window = this@Pomodoro.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.red)

        prefs =
            this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        if (currentRound.text.toString() == pomodoroRound.text.toString()) {
            round = 0
        }
        if (currentGoal.text.toString() == pomodoroDailyGoals.text.toString()) {
            pomodoroGoalCompleted.visibility = View.VISIBLE
        } else {
            pomodoroGoalCompleted.visibility = View.GONE
        }
        pomodoroMenu.setOnClickListener {
            pomodoroMenu.visibility = View.GONE
            musicPomodoro.visibility = View.GONE
            musicPomodoroText.visibility = View.GONE
            strictMode.visibility = View.GONE
            strictModeText.visibility = View.GONE
            startFocus.visibility = View.GONE
            pomodoroTimer.visibility = View.GONE
            roundAndGoal.visibility = View.GONE
            pomodoroSettingScreen.visibility = View.VISIBLE
            window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.pomodoro_white)
        }
        pomodoroBack.setOnClickListener {
            if (pomodoroRound.text.toString().toInt() > 0) {
                definedRound.text = "/${pomodoroRound.text}"
            }
            if (pomodoroDailyGoals.text.toString().toInt() > 0) {
                definedGoal.text = "/${pomodoroDailyGoals.text}"
            }
            if (pomodoroSession.text.toString().toInt() > 0) {
                pomodoroTimer.text = "${pomodoroSession.text} : 00"
            }
            pomodoroMenu.visibility = View.VISIBLE
            startFocus.visibility = View.VISIBLE
            pomodoroTimer.visibility = View.VISIBLE
            roundAndGoal.visibility = View.VISIBLE
            pomodoroSettingScreen.visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.red)
        }

        startFocus.setOnClickListener {
            if (background == 1) {
                if (pomodoroSession.text.toString().toInt() > 0) {
                    timeInMilliSeconds = pomodoroSession.text.toString().toLong() * 60000
                }
                startTimer(timeInMilliSeconds)
                startFocus.text = "Pause"
                startFocus.setBackgroundResource(R.drawable.mood_border_text)
                background = 2
            } else if (background == 2) {
                pauseTimer()
                startFocus.visibility = View.GONE
                pomodoroButtons.visibility = View.VISIBLE
                background = 3
            }
        }
        stop.setOnClickListener {
            resetTimer()
            startFocus.visibility = View.VISIBLE
            startFocus.text = "Start to Focus"
            startFocus.setBackgroundResource(R.drawable.drink_button)
            pomodoroButtons.visibility = View.INVISIBLE
            background = 1
        }
        continuePomodoro.setOnClickListener {
            startTimer(timeInMilliSeconds)
            startFocus.visibility = View.VISIBLE
            pomodoroButtons.visibility = View.INVISIBLE
            background = 2
        }
        musicPomodoro.setOnClickListener {
            pomodoroMenu.visibility = View.GONE
            startFocus.visibility = View.GONE
            pomodoroTimer.visibility = View.GONE
            roundAndGoal.visibility = View.GONE
            musicPomodoroMenu.visibility = View.VISIBLE
        }
        musicPomodoroText.setOnClickListener {
            pomodoroMenu.visibility = View.GONE
            startFocus.visibility = View.GONE
            pomodoroTimer.visibility = View.GONE
            roundAndGoal.visibility = View.GONE
            musicPomodoroMenu.visibility = View.VISIBLE
        }
        musicPomodoroConfirm.setOnClickListener {
            pomodoroMenu.visibility = View.VISIBLE
            startFocus.visibility = View.VISIBLE
            pomodoroTimer.visibility = View.VISIBLE
            roundAndGoal.visibility = View.VISIBLE
            musicPomodoroMenu.visibility = View.GONE
        }
    }

    private fun pauseTimer() {
        countdownTimer.cancel()
    }

    fun startTimer(time_in_seconds: Long) {
        countdownTimer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                if (currentGoal.text.toString() == pomodoroDailyGoals.text.toString()) {
                    pomodoroGoalCompleted.visibility = View.VISIBLE
                } else {
                    pomodoroGoalCompleted.visibility = View.GONE
                }
                if (currentRound.text.toString() == pomodoroRound.text.toString()) {
                    finish = 3
                }
                when (finish) {
                    1 -> {
                        if (shortBreak.text.toString().toInt() > 0) {
                            timeInMilliSeconds =
                                shortBreak.text.toString().toLong() * 60000
                        }
                        pomodoroBackground.setBackgroundResource(R.color.emerald)
                        val window: Window = this@Pomodoro.window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.emerald)
                        startTimer(timeInMilliSeconds)
                        finish = 2
                    }
                    2 -> {
                        goals += 1
                        round += 1
                        currentRound.text = round.toString()
                        currentGoal.text = goals.toString()
                        finish = if (currentRound.text.toString() == pomodoroRound.text.toString()) {
                            3
                        } else {
                            1
                        }
                        if (pomodoroSession.text.toString().toInt() > 0) {
                            timeInMilliSeconds = pomodoroSession.text.toString().toLong() * 60000
                        }
                        pomodoroBackground.setBackgroundResource(R.color.red)
                        val window: Window = this@Pomodoro.window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.red)
                        startTimer(timeInMilliSeconds)
                    }
                    else -> {
                        round = 0
                        currentRound.text = round.toString()
                        if (longBreak.text.toString().toInt() > 0) {
                            timeInMilliSeconds = longBreak.text.toString().toLong() * 60000
                        }
                        pomodoroBackground.setBackgroundResource(R.color.sari)
                        val window: Window = this@Pomodoro.window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.sari)
                        startTimer(timeInMilliSeconds)
                        finish = 2
                    }
                }
            }

            override fun onTick(p0: Long) {
                timeInMilliSeconds = p0
                updateTextUI()
            }
        }
        countdownTimer.start()

    }

    @SuppressLint("SetTextI18n")
    private fun resetTimer() {
        if (pomodoroSession.text.toString().toInt() > 0) {
            pomodoroTimer.text = "${pomodoroSession.text} : 00"
        }
        pomodoroBackground.setBackgroundResource(R.color.red)
        val window: Window = this@Pomodoro.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@Pomodoro, R.color.red)
    }

    @SuppressLint("SetTextI18n")
    internal fun updateTextUI() {
        val minute = (timeInMilliSeconds / 1000) / 60
        val seconds = (timeInMilliSeconds / 1000) % 60
        if (seconds < 10) {
            pomodoroTimer.text = "$minute : 0$seconds"
        } else {
            pomodoroTimer.text = "$minute : $seconds"
        }
    }
}