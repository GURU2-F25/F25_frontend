package com.example.f25_frontend.ui.todo

import android.annotation.SuppressLint
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
import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.adapter.CategoryAdapter
import com.example.f25_frontend.model.CategoryDto
import com.example.f25_frontend.model.TaskDto
import com.example.f25_frontend.adapter.WeekAdapter
import com.example.f25_frontend.utils.ApiClient
import com.example.f25_frontend.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import com.example.f25_frontend.viewmodel.CategoryViewModel


/*
    @Author 조수연, 김소연
    @TODO 내 일정 서버 연동 개발 예정
*/
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

        fetchCategoriesAndTasks()

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
            findNavController().navigate(R.id.action_todoFragment_to_categorySettingFragment)
        }
    }

    @SuppressLint("SetTextI18n")
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

    private fun fetchCategoriesAndTasks() {
        val userId = MyApplication.prefs.getString("id") ?: return
        val service: RetrofitUtil = ApiClient.getAuthApiClient().create(RetrofitUtil::class.java)
        service.getCategory(userId)
            .enqueue(object : Callback<List<CategoryDto>> {
                override fun onResponse(
                    call: Call<List<CategoryDto>>,
                    response: Response<List<CategoryDto>>
                ) {
                    val categories = response.body()?.onEach {
                        it.tasksByDate = mutableMapOf()
                    }?.toMutableList() ?: mutableListOf()

                    service.getTodo(userId, selectedDate.toString())
                        .enqueue(object : Callback<List<TaskDto>> {
                            override fun onResponse(
                                call: Call<List<TaskDto>>,
                                response: Response<List<TaskDto>>
                            ) {
                                val taskList = response.body() ?: emptyList()
                                for (task in taskList) {
                                    val category = categories.find { it.id == task.categoryId }
                                    if (category != null) {
                                        val taskDate = task.date
                                        if (!category.tasksByDate.containsKey(taskDate)) {
                                            category.tasksByDate[taskDate] = mutableListOf()
                                        }
                                        category.tasksByDate[taskDate]?.add(task)
                                    }
                                }
                                categoryViewModel.setCategoriesForSelectedDate(categories)
                            }

                            override fun onFailure(call: Call<List<TaskDto>>, t: Throwable) {}
                        })
                }

                override fun onFailure(call: Call<List<CategoryDto>>, t: Throwable) {}
            })
    }

    private fun shiftWeek(weeks: Long) {
        currentWeekStart = currentWeekStart.plusWeeks(weeks)
        setupWeekView()
        categoryViewModel.updateSelectedDate(currentWeekStart)
    }

    private fun showAddTaskDialog(categoryDto: CategoryDto) {
        val dialog = AddTaskDialog(
            context = requireContext(),
            categoryDto = categoryDto,
            date = selectedDate,
            onTaskAdded = {
                categoryViewModel.updateSelectedDate(selectedDate)
            }
        )
        dialog.show()
    }

    private fun refreshTaskListForDate(date: LocalDate) {}

    @SuppressLint("SetTextI18n")
    private fun updateCategoryProgress(categories: List<CategoryDto>) {
        categoryProgressContainer.removeAllViews()

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
            progressBar.progressTintList = ColorStateList.valueOf(category.color)
            tvPercent.text = "$percent%"
            tvPercent.setTextColor(category.color)

            categoryProgressContainer.addView(progressLayout)
        }
    }
}