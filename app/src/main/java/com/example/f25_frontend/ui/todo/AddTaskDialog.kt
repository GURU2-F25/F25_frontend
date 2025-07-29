package com.example.f25_frontend.ui.todo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import com.example.f25_frontend.R
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import java.time.LocalDate

class AddTaskDialog(
    context: Context,
    private val category: Category,
    private val date: LocalDate,
    private val onTaskAdded: () -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_task)


        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvCategoryTitle = findViewById<TextView>(R.id.tvCategoryTitle)
        val etTaskTitle = findViewById<EditText>(R.id.etTaskTitle)
        val cbRepeatDaily = findViewById<CheckBox>(R.id.cbRepeatDaily)
        val cbRepeatWeekly = findViewById<CheckBox>(R.id.cbRepeatFriday)
        val btnAddTaskConfirm = findViewById<Button>(R.id.btnAddTaskConfirm)

        tvCategoryTitle.text = category.name

        btnAddTaskConfirm.setOnClickListener {
            val taskTitle = etTaskTitle.text.toString().trim()

            if (taskTitle.isEmpty()) {
                Toast.makeText(context, "일정명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repeatDaily = cbRepeatDaily.isChecked
            val repeatWeekly = cbRepeatWeekly.isChecked

            when {
                repeatDaily -> {
                    val startOfWeek = date.with(java.time.DayOfWeek.MONDAY)
                    for (i in 0 until 7) {
                        val day = startOfWeek.plusDays(i.toLong())
                        val task = Task(
                            title = taskTitle,
                            date = day,
                            category = category,
                            isDone = false
                        )
                        category.tasksByDate.getOrPut(day) { mutableListOf() }.add(task)
                    }
                }

                repeatWeekly -> {
                    for (i in 0 until 4) { // 4주 반복
                        val weeklyDate = date.plusWeeks(i.toLong())
                        val task = Task(
                            title = taskTitle,
                            date = weeklyDate,
                            category = category,
                            isDone = false
                        )
                        category.tasksByDate.getOrPut(weeklyDate) { mutableListOf() }.add(task)
                    }
                }

                else -> {
                    val task = Task(
                        title = taskTitle,
                        date = date,
                        category = category,
                        isDone = false
                    )
                    category.tasksByDate.getOrPut(date) { mutableListOf() }.add(task)
                }
            }

            onTaskAdded()
            dismiss()
        }
    }
}
