package com.example.taskmaster

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class FocusMode : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var startStopButton: Button
    private lateinit var resetButton: Button

    private val focusTimeMillis: Long = 30 * 60 * 1000 // 30 minutes
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftMillis: Long = focusTimeMillis
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set immersive mode for edge-to-edge experience
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContentView(R.layout.activity_focus_mode)

        // Initialize UI elements
        progressBar = findViewById(R.id.progressBarCircular)
        timerText = findViewById(R.id.timerText)
        startStopButton = findViewById(R.id.startStopButton)
        resetButton = findViewById(R.id.resetButton)

        // Set the progress bar to full initially (max)
        progressBar.max = (focusTimeMillis / 1000).toInt()
        progressBar.progress = progressBar.max

        // Start/Stop Button Click Listener
        startStopButton.setOnClickListener {
            if (isTimerRunning) {
                stopFocusMode()
            } else {
                startFocusMode()
            }
        }

        // Reset Button Click Listener
        resetButton.setOnClickListener {
            resetFocusMode()
        }
    }

    private fun resetFocusMode() {
        stopFocusMode() // Stop the timer if it's running
        timeLeftMillis = focusTimeMillis // Reset time
        updateTimerUI() // Reset UI
    }

    private fun startFocusMode() {
        countDownTimer = object : CountDownTimer(timeLeftMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                timeLeftMillis = 0
                updateTimerUI()
                stopFocusMode() // Automatically stop when the timer finishes
            }
        }.start()

        isTimerRunning = true
        startStopButton.text = "Stop Focus"
    }

    private fun stopFocusMode() {
        countDownTimer.cancel() // Cancel the timer
        isTimerRunning = false
        startStopButton.text = "Start Focus"
    }

    private fun updateTimerUI() {
        val minutes = (timeLeftMillis / 1000) / 60
        val seconds = (timeLeftMillis / 1000) % 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)

        // Update progress bar based on remaining time
        val progress = (timeLeftMillis / 1000).toInt()
        progressBar.progress = progress // Decrease the progress bar over time
    }
}
