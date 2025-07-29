package com.example.f25_frontend.ui.todo

import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.f25_frontend.databinding.FragmentTodoBinding
import com.example.f25_frontend.R
import com.example.f25_frontend.CategoryAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.UserDto
import retrofit2.converter.gson.GsonConverterFactory


class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

//    private lateinit var weekCalendarView: WeekCalendarView
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: CategoryAdapter
//    private lateinit var tvMonthYear: TextView
//
//    private var selectedDate: LocalDate? = null
//    private val categoryList = mutableListOf(
//        Category("카테고리 1"),
//        Category("카테고리 2"),
//        Category("카테고리 3")
//    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        Log.d("resultTodo:", MyApplication.prefs.getString("access_token"))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun setupWeekCalendar() {
//        val today = LocalDate.now()
//        val startDate = today.minusWeeks(52)
//        val endDate = today.plusWeeks(52)
//
////        weekCalendarView.setup(startDate, endDate, java.time.DayOfWeek.MONDAY)
////        weekCalendarView.scrollToWeek(today)
////        tvMonthYear.text = "${today.year}년 ${today.monthValue}월"
////
////        weekCalendarView.dayBinder =
////            object : com.kizitonwose.calendar.view.WeekDayBinder<WeekDayContainer> {
////                override fun create(view: View) = WeekDayContainer(view)
////                override fun bind(container: WeekDayContainer, data: WeekDay) {
////                    val date = data.date
////                    container.day = data
////
////                    container.dayText.text = date.dayOfMonth.toString()
////                    container.weekDayText.text =
////                        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
////
////                    // 날짜 클릭 → 선택 표시 + 선택 날짜 저장
////                    container.view.setOnClickListener {
////                        selectedDate = date
////                        weekCalendarView.notifyCalendarChanged()
////                    }
////
////                    // 선택된 날짜만 회색 배경
////                    if (selectedDate == date) {
////                        container.view.setBackgroundResource(R.drawable.bg_selected_date)
////                    } else {
////                        container.view.background = null
////                    }
////                }
////            }
////
////        // 스와이프 시 상단 년/월 변경
////        weekCalendarView.weekScrollListener = { week ->
////            val midDate = week.days[3].date
////            tvMonthYear.text = "${midDate.year}년 ${midDate.monthValue}월"
////        }
//    }

//    private fun setupCategoryList() {
//        adapter = CategoryAdapter(categoryList) { category ->
//            // 선택된 날짜가 없으면 오늘 날짜로
//            val targetDate = selectedDate ?: LocalDate.now()
//
//            val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
//            val etTaskTitle = dialogView.findViewById<android.widget.EditText>(R.id.etTaskTitle)
//
////            val dialog = AlertDialog.Builder(this)
////                .setView(dialogView)
////                .create()
////
////            dialogView.findViewById<android.widget.Button>(R.id.btnAddTask).setOnClickListener {
////                val title = etTaskTitle.text.toString().trim()
////                if (title.isNotEmpty()) {
////                    val newTask = Task(title, targetDate)
////                    category.tasks.add(newTask) // 선택된 카테고리에만 추가
////                    recyclerView.adapter?.notifyDataSetChanged()
////                    dialog.dismiss()
////                }
////            }
////            dialog.show()
//        }
//
////        recyclerView.layoutManager = LinearLayoutManager(this)
////        recyclerView.adapter = adapter
//    }
}

//package com.example.f25_frontend.todolist
//
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.f25_frontend.model.Category
//import com.example.f25_frontend.model.Task
//import com.kizitonwose.calendar.core.WeekDay
//import com.kizitonwose.calendar.view.WeekCalendarView
//import java.time.LocalDate
//import java.time.format.TextStyle
//import java.util.Locale
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var weekCalendarView: WeekCalendarView
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: CategoryAdapter
//    private lateinit var tvMonthYear: TextView
//
//    private var selectedDate: LocalDate? = null
//    private val categoryList = mutableListOf(
//        Category("카테고리 1"),
//        Category("카테고리 2"),
//        Category("카테고리 3")
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        weekCalendarView = findViewById(R.id.weekCalendarView)
//        recyclerView = findViewById(R.id.categoryRecyclerView)
//        tvMonthYear = findViewById(R.id.tvMonthYear)
//
//        setupWeekCalendar()
//        setupCategoryList()
//    }
//
//    private fun setupWeekCalendar() {
//        val today = LocalDate.now()
//        val startDate = today.minusWeeks(52)
//        val endDate = today.plusWeeks(52)
//
//        weekCalendarView.setup(startDate, endDate, java.time.DayOfWeek.MONDAY)
//        weekCalendarView.scrollToWeek(today)
//        tvMonthYear.text = "${today.year}년 ${today.monthValue}월"
//
//        weekCalendarView.dayBinder =
//            object : com.kizitonwose.calendar.view.WeekDayBinder<WeekDayContainer> {
//                override fun create(view: View) = WeekDayContainer(view)
//                override fun bind(container: WeekDayContainer, data: WeekDay) {
//                    val date = data.date
//                    container.day = data
//
//                    container.dayText.text = date.dayOfMonth.toString()
//                    container.weekDayText.text =
//                        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
//
//                    // 날짜 클릭 → 선택 표시 + 선택 날짜 저장
//                    container.view.setOnClickListener {
//                        selectedDate = date
//                        weekCalendarView.notifyCalendarChanged()
//                    }
//
//                    // 선택된 날짜만 회색 배경
//                    if (selectedDate == date) {
//                        container.view.setBackgroundResource(R.drawable.bg_selected_date)
//                    } else {
//                        container.view.background = null
//                    }
//                }
//            }
//
//        // 스와이프 시 상단 년/월 변경
//        weekCalendarView.weekScrollListener = { week ->
//            val midDate = week.days[3].date
//            tvMonthYear.text = "${midDate.year}년 ${midDate.monthValue}월"
//        }
//    }
//
//    private fun setupCategoryList() {
//        adapter = CategoryAdapter(categoryList) { category ->
//            // 선택된 날짜가 없으면 오늘 날짜로
//            val targetDate = selectedDate ?: LocalDate.now()
//
//            val dialogView = layoutInflater.inflate(R.layout.dialog_add_task, null)
//            val etTaskTitle = dialogView.findViewById<android.widget.EditText>(R.id.etTaskTitle)
//
//            val dialog = AlertDialog.Builder(this)
//                .setView(dialogView)
//                .create()
//
//            dialogView.findViewById<android.widget.Button>(R.id.btnAddTask).setOnClickListener {
//                val title = etTaskTitle.text.toString().trim()
//                if (title.isNotEmpty()) {
//                    val newTask = Task(title, targetDate)
//                    category.tasks.add(newTask) // 선택된 카테고리에만 추가
//                    recyclerView.adapter?.notifyDataSetChanged()
//                    dialog.dismiss()
//                }
//            }
//            dialog.show()
//        }
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
//    }
//}
