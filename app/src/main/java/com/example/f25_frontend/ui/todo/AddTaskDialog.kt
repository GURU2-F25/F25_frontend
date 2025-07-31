package com.example.f25_frontend.ui.todo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import com.example.f25_frontend.R
import com.example.f25_frontend.model.CategoryDto
import com.example.f25_frontend.model.TaskDto
import java.time.LocalDate
/*
    @Author 김소연
*/
class AddTaskDialog(
    context: Context,
    private val categoryDto: CategoryDto,
    private val date: LocalDate,
    private val onTaskAdded: () -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_task)

        // ✅ 다이얼로그 크기 및 배경 설정
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        // ✅ UI 요소 참조
        val tvCategoryTitle = findViewById<TextView>(R.id.tvCategoryTitle)
        val etTaskTitle = findViewById<EditText>(R.id.etTaskTitle)
        val cbRepeatDaily = findViewById<CheckBox>(R.id.cbRepeatDaily)
        val cbRepeatWeekly = findViewById<CheckBox>(R.id.cbRepeatFriday)  // 이름이 Weekly여도 id는 Friday
        val btnAddTaskConfirm = findViewById<Button>(R.id.btnAddTaskConfirm)

        tvCategoryTitle.text = categoryDto.name

        // ✅ 버튼 클릭 처리
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
                        val taskDto = TaskDto(
                            title = taskTitle,
                            date = day,
                            categoryDto = categoryDto,
                            isDone = false
                        )
                        categoryDto.tasksByDate.getOrPut(day) { mutableListOf() }.add(taskDto)
                    }
                }

                repeatWeekly -> {
                    for (i in 0 until 4) { // 4주 반복
                        val weeklyDate = date.plusWeeks(i.toLong())
                        val taskDto = TaskDto(
                            title = taskTitle,
                            date = weeklyDate,
                            categoryDto = categoryDto,
                            isDone = false
                        )
                        categoryDto.tasksByDate.getOrPut(weeklyDate) { mutableListOf() }.add(taskDto)
                    }
                }

                else -> {
                    val taskDto = TaskDto(
                        title = taskTitle,
                        date = date,
                        categoryDto = categoryDto,
                        isDone = false
                    )
                    categoryDto.tasksByDate.getOrPut(date) { mutableListOf() }.add(taskDto)
                }
            }

            onTaskAdded()
            dismiss()
        }
    }
}
