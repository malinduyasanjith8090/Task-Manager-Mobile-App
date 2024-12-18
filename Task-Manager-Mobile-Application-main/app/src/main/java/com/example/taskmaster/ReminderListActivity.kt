package com.example.taskmaster

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.databinding.ActivityReminderListBinding

class ReminderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load and display stored reminders
        loadReminder()

        // Delete reminder on button click
        binding.deleteButton.setOnClickListener {
            deleteReminder()
            // Optionally clear the UI after deletion
            binding.taskTitle.text = "No reminder"
            binding.taskMessage.text = "No message"
        }
    }

    private fun loadReminder() {
        val sharedPreferences = getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)
        val title = sharedPreferences.getString("reminderTitle", "No title")
        val message = sharedPreferences.getString("reminderMessage", "No message")

        // Set the retrieved title and message to the UI
        binding.taskTitle.text = title
        binding.taskMessage.text = message
    }

    private fun deleteReminder() {
        val sharedPreferences = getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Clear all stored reminders
        editor.apply()
    }
}
