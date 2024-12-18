package com.example.taskmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Home : AppCompatActivity() {

    private val tasks = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var illustration: View
    private lateinit var taskPrompt: View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val fabAddTask = findViewById<FloatingActionButton>(R.id.fabAddTask)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        illustration = findViewById(R.id.illustration)
        taskPrompt = findViewById(R.id.taskPrompt)

        recyclerView = findViewById(R.id.recyclerView)
        taskAdapter = TaskAdapter(tasks)
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up swipe to delete and edit
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            private val deleteBackground = ColorDrawable(Color.RED)
            private val editBackground = ColorDrawable(Color.GREEN)
            private val clearPaint = Paint().apply {
                xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    taskAdapter.removeItem(position)  // For delete
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showEditTaskDialog(position, tasks[position])  // For edit
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                // Swiping left (delete action)
                if (dX < 0) {
                    deleteBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    deleteBackground.draw(c)
                }
                // Swiping right (edit action)
                else if (dX > 0) {
                    editBackground.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX.toInt(),
                        itemView.bottom
                    )
                    editBackground.draw(c)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun onChildDrawOver(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                val itemView = viewHolder.itemView
                val canvas = Canvas()
                canvas.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    clearPaint
                )
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_index -> {
                    startActivity(Intent(this, Home::class.java))
                    true
                }
                R.id.nav_calendar -> {
                    startActivity(Intent(this, Reminder::class.java))
                    true
                }
                R.id.nav_focus -> {
                    startActivity(Intent(this, FocusMode::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateUI()
    }

    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        val editTextTask = dialogView.findViewById<EditText>(R.id.editTextTask)

        AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val taskTitle = editTextTask.text.toString()
                if (taskTitle.isNotEmpty()) {
                    tasks.add(Task(taskTitle))
                    taskAdapter.notifyItemInserted(tasks.size - 1)
                    updateUI()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showEditTaskDialog(position: Int, task: Task) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
        val editTextTask = dialogView.findViewById<EditText>(R.id.editTextTask)
        editTextTask.setText(task.title)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(dialogView)
            .setPositiveButton("Update") { dialog, _ ->
                val updatedTaskTitle = editTextTask.text.toString()
                if (updatedTaskTitle.isNotEmpty()) {
                    tasks[position] = Task(updatedTaskTitle)
                    taskAdapter.notifyItemChanged(position)
                    updateUI()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun updateUI() {
        if (tasks.isEmpty()) {
            illustration.visibility = View.VISIBLE
            taskPrompt.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            illustration.visibility = View.GONE
            taskPrompt.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
