package com.example.f25_frontend.ui.todo

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.R
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import com.example.f25_frontend.ui.adapter.WeekAdapter
import com.example.f25_frontend.ui.dialog.AddTaskDialog
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.retrofitUtil
import com.example.f25_frontend.viewmodel.CategoryViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate

class TodoFragment : Fragment() {

    private lateinit var weekRecyclerView: RecyclerView
    private lateinit var tvMonthYear: TextView
    private lateinit var btnPrevWeek: ImageButton
    private lateinit var btnNextWeek: ImageButton
    private lateinit var btnEditCategory: ImageButton
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryProgressContainer: LinearLayout
    private lateinit var tvSelectedDateProgress: TextView

    private lateinit var weekAdapter: WeekAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private var selectedDate: LocalDate = LocalDate.now()
    private var currentWeekStart: LocalDate = LocalDate.now().with(DayOfWeek.MONDAY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        hardcoded test
        val service: retrofitUtil = ApiClient.getNoAuthApiClient().create(retrofitUtil::class.java)
        service.getTasks("20250730")
            .enqueue(object: Callback<List<Task>> {
                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                    service.getCategories("20250730")
                        .enqueue(object: Callback<List<Category>> {
                            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
//                             FIND LAYOUT & ADD ITEM NEEDS
//                             FIND LAYOUT & ADD ITEM NEEDS
//                             FIND LAYOUT & ADD ITEM NEEDS
                            }
                            override fun onFailure(call: Call<List<Category>>, t: Throwable) {

                            }
                        })
                }
                override fun onFailure(call: Call<List<Task>>, t: Throwable) {

                }
            })
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        weekRecyclerView = view.findViewById(R.id.weekRecyclerView)
        tvMonthYear = view.findViewById(R.id.tvMonthYear)
        btnPrevWeek = view.findViewById(R.id.btnPrevWeek)
        btnNextWeek = view.findViewById(R.id.btnNextWeek)
        btnEditCategory = view.findViewById(R.id.btnEditCategory)
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView)
        categoryProgressContainer = view.findViewById(R.id.categoryProgressContainer)
        tvSelectedDateProgress = view.findViewById(R.id.tvSelectedDateProgress)

        setupWeekView()
        setupCategoryList()

        categoryViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            selectedDate = date
            weekAdapter.updateSelectedDate(date)
            categoryAdapter.updateSelectedDate(date)
        }

        categoryViewModel.categoriesForSelectedDate.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories)
            updateCategoryProgress(categories)
        }

        btnPrevWeek.setOnClickListener { shiftWeek(-1) }
        btnNextWeek.setOnClickListener { shiftWeek(1) }
        btnEditCategory.setOnClickListener {
//            해당 부분 navigate가 아닌 dialog로 작성
//                    참고 -> https://developer.android.com/develop/ui/views/components/dialogs?hl=ko
            findNavController().navigate(R.id.action_todoFragment_to_categorySettingFragment)
        }
    }

    private fun setupWeekView() {
        val weekList = (0 until 7).map { currentWeekStart.plusDays(it.toLong()) }
        tvMonthYear.text = "${currentWeekStart.year}년 ${currentWeekStart.monthValue}월"
        val screenWidth = resources.displayMetrics.widthPixels

        weekAdapter = WeekAdapter(weekList, screenWidth) { date ->
            categoryViewModel.updateSelectedDate(date)
            refreshTaskListForDate(date)
        }

        weekRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        weekRecyclerView.adapter = weekAdapter
    }

    private fun setupCategoryList() {
        categoryAdapter = CategoryAdapter(
            categories = emptyList(),
            selectedDate = selectedDate,
            onAddTaskClick = { category ->
                showAddTaskDialog(category)
            },
            onTaskChecked = { task ->
                task.isDone = !task.isDone
                categoryViewModel.updateSelectedDate(selectedDate)
            },
            onTaskDeleted = { task ->
                categoryViewModel.removeTask(selectedDate, task)
                categoryViewModel.updateSelectedDate(selectedDate)
            }
        )

        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryRecyclerView.adapter = categoryAdapter
    }

    private fun shiftWeek(weeks: Long) {
        currentWeekStart = currentWeekStart.plusWeeks(weeks)
        setupWeekView()
        categoryViewModel.updateSelectedDate(currentWeekStart)
    }

    private fun showAddTaskDialog(category: Category) {
        val dialog = AddTaskDialog.newInstance(category, selectedDate)

        dialog.setOnTaskAddedListener { task, repeatDaily, repeatWeekly ->
            when {
                repeatDaily -> {
                    // 1주간 (월~일) 반복
                    val weekStart = selectedDate.with(java.time.DayOfWeek.MONDAY)
                    for (i in 0..6) {
                        val day = weekStart.plusDays(i.toLong())
                        category.tasksByDate.getOrPut(day) { mutableListOf() }
                            .add(task.copy(date = day))
                    }
                }

                repeatWeekly -> {
                    // 같은 요일 기준 4주 반복
                    val originalDayOfWeek = selectedDate.dayOfWeek
                    for (i in 0..3) {
                        val weekDate = selectedDate.plusWeeks(i.toLong())
                        val sameWeekday = weekDate.with(originalDayOfWeek)
                        category.tasksByDate.getOrPut(sameWeekday) { mutableListOf() }
                            .add(task.copy(date = sameWeekday))
                    }
                }

                else -> {
                    // 단일 일정
                    category.tasksByDate.getOrPut(selectedDate) { mutableListOf() }
                        .add(task)
                }
            }

            categoryViewModel.updateSelectedDate(selectedDate)
        }

        dialog.show(parentFragmentManager, "AddTaskDialog")
    }





    private fun refreshTaskListForDate(date: LocalDate) {}

    private fun updateCategoryProgress(categories: List<Category>) {
        categoryProgressContainer.removeAllViews()

        // 전체 목표 달성률 계산
        val totalTasks = categories.sumOf { it.tasksByDate[selectedDate]?.size ?: 0 }
        val doneTasks = categories.sumOf { it.tasksByDate[selectedDate]?.count { it.isDone } ?: 0 }

        if (totalTasks > 0) {
            val percent = (doneTasks * 100) / totalTasks
            tvSelectedDateProgress.text = "${selectedDate.dayOfMonth}일의 목표 달성률 ${percent}%"
            tvSelectedDateProgress.setTextColor(Color.BLACK)
        } else {
            tvSelectedDateProgress.text = "${selectedDate.dayOfMonth}일은 등록된 일정이 없습니다"
            tvSelectedDateProgress.setTextColor(Color.GRAY)
        }

        for (category in categories) {
            val tasks = category.tasksByDate[selectedDate] ?: emptyList()
            if (tasks.isEmpty()) continue

            val done = tasks.count { it.isDone }
            val percent = (done * 100) / tasks.size

            val progressLayout = LayoutInflater.from(context)
                .inflate(R.layout.item_category_progress, categoryProgressContainer, false)

            val tvName = progressLayout.findViewById<TextView>(R.id.tvCategoryName)
            val progressBar = progressLayout.findViewById<ProgressBar>(R.id.progressBar)
            val tvPercent = progressLayout.findViewById<TextView>(R.id.tvProgressPercent)

            tvName.text = category.name
            tvName.setTextColor(category.color)

            progressBar.progress = percent
            progressBar.progressTintList = ColorStateList.valueOf(category.color) // ✅ 색상 적용
            tvPercent.text = "$percent%"
            tvPercent.setTextColor(category.color)

            categoryProgressContainer.addView(progressLayout)
        }
    }
}
