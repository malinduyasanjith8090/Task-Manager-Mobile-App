package com.example.taskmaster

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Get references to EditText fields and Button
        val usernameEditText = findViewById<EditText>(R.id.UserName)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val loginButton = findViewById<Button>(R.id.button3)

        loginButton.setOnClickListener {
            val enteredUsername = usernameEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            // Retrieve stored data
            val savedUsername = sharedPreferences.getString("username", null)
            val savedPassword = sharedPreferences.getString("password", null)

            // Validate user input
            if (enteredUsername == savedUsername && enteredPassword == savedPassword) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show()
            }
            val onboard3: Button = findViewById(R.id.button3)
            onboard3.setOnClickListener {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
            val onboard: TextView = findViewById(R.id.GuestLogin)
            onboard.setOnClickListener {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
        }
    }
}
