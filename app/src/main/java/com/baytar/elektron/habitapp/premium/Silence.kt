package com.baytar.elektron.habitapp.premium

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.baytar.elektron.habitapp.R
import com.baytar.elektron.habitapp.databinding.SilenceBinding
import com.baytar.elektron.habitapp.premium.util.PrefUtil
import com.baytar.elektron.habitapp.premium.util.SettingsActivity
import java.util.*
import kotlin.random.Random

/**
 *
 */
class Silence : AppCompatActivity() {
    private lateinit var binding: SilenceBinding

    private lateinit var playTimer: ImageView
    private lateinit var stop: ImageView
    private lateinit var pause: ImageView
    private lateinit var meditationHeader: TextView
    private lateinit var meditationQuota: TextView
    private lateinit var setting: ImageView
    private lateinit var time: TextView

    companion object {
        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    /**
     *
     */
    enum class State {

        Running, Stopped, Pause
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = State.Stopped
    var secondsRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SilenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playTimer = binding.playTimer
        stop = binding.stop
        pause = binding.pause
        meditationHeader = binding.meditationHeader
        meditationQuota = binding.meditationQuota
        setting = binding.setting
        time = binding.time
        val window: Window = this@Silence.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this@Silence, R.color.prussianblue)
        val quota1 = R.string.meditation1
        val quota2 = R.string.meditation2
        val quota3 = R.string.meditation3
        val quota4 = R.string.meditation4
        val quota5 = R.string.meditation5
        val meditationList = listOf(quota1, quota2, quota3, quota4, quota5)
        val random = Random.nextInt(meditationList.size)
        meditationQuota.setText(meditationList[random])
        val header1 = R.string.header1
        val header2 = R.string.header2
        val header3 = R.string.header3
        val header4 = R.string.header4
        val header5 = R.string.header5
        val headerList = listOf(header1, header2, header3, header4, header5)
        val randomHeader = Random.nextInt(headerList.size)
        meditationHeader.setText(headerList[randomHeader])

        playTimer.setOnClickListener {
            startTimer()
            timerState = State.Running
            updateButtons()
        }
        pause.setOnClickListener {
            timer.cancel()
            timerState = State.Pause
            updateButtons()
        }
        stop.setOnClickListener {
            timer.cancel()
            onTimerFinished()

        }
        setting.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }

    }

    override fun onResume() {
        super.onResume()
        initTimer()

    }

    override fun onPause() {
        super.onPause()
        if (timerState == State.Running) {
            timer.cancel()         //TODO: start background timer and show notification
        } else if (timerState == State.Pause) {
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == State.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == State.Running || timerState == State.Pause)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds


        //resume where we left off
        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == State.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    fun onTimerFinished() {
        timerState = State.Stopped

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerLength()

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = State.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
    }

    @SuppressLint("SetTextI18n")
    internal fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        time.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
    }

    private fun updateButtons() {
        when (timerState) {
            State.Running -> {
                playTimer.isEnabled = false
                pause.isEnabled = true
                stop.isEnabled = true
            }
            State.Stopped -> {
                playTimer.isEnabled = true
                pause.isEnabled = false
                stop.isEnabled = false
            }
            State.Pause -> {
                playTimer.isEnabled = true
                pause.isEnabled = false
                stop.isEnabled = true
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.setting -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}