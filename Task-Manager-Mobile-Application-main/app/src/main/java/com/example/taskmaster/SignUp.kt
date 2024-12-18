package com.example.taskmaster

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)

        // Get references to EditText fields and Button
        val usernameEditText = findViewById<EditText>(R.id.UserName)
        val emailEditText = findViewById<EditText>(R.id.Email)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val reenterPasswordEditText = findViewById<EditText>(R.id.ReenterPassword)
        val submitButton = findViewById<Button>(R.id.button)

        submitButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val rePassword = reenterPasswordEditText.text.toString()

            // Check if all fields are filled
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == rePassword) {
                // Save data to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.putString("email", email)
                editor.putString("password", password)
                editor.apply()

                Toast.makeText(this, "User data saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill all fields or passwords do not match!", Toast.LENGTH_SHORT).show()
            }
            val onboard3: Button = findViewById(R.id.button)
            onboard3.setOnClickListener {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }

        }
        val AlreadyRegister: TextView = findViewById(R.id.alreadyregister)
        AlreadyRegister.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

}
