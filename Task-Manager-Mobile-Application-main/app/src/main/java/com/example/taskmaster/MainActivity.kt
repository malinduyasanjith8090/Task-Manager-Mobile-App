package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


            // Navigate to SecondActivity after 3 seconds
            Handler().postDelayed({
                val intent = Intent(this, Onboard_1::class.java)
                startActivity(intent)
                finish() // Optionally close this activity so it won’t return on back press
            }, 3000) // 3000 milliseconds = 3 seconds


        }
    }
