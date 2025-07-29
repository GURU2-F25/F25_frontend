package com.example.f25_frontend.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import java.time.LocalDate

class CategoryViewModel : ViewModel() {

    private val categoryMap = mutableMapOf<LocalDate, MutableList<Category>>()

    private val _selectedDate = MutableLiveData<LocalDate>(LocalDate.now())
    val selectedDate: LiveData<LocalDate> get() = _selectedDate

    private val _categoriesForSelectedDate = MutableLiveData<List<Category>>()
    val categoriesForSelectedDate: LiveData<List<Category>> get() = _categoriesForSelectedDate

    private val defaultCategories = listOf(
        Category(name = "카테고리1", color = Color.parseColor("#A7D397")),
        Category(name = "카테고리2", color = Color.parseColor("#FCDC58")),
        Category(name = "카테고리3", color = Color.parseColor("#FF9B9B"))
    )

    init {
        val today = _selectedDate.value ?: LocalDate.now()
        categoryMap[today] = defaultCategories.map { it.copy() }.toMutableList()
        _categoriesForSelectedDate.value = categoryMap[today]
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        if (!categoryMap.containsKey(date)) {
            categoryMap[date] = defaultCategories.map { it.copy() }.toMutableList()
        }
        _categoriesForSelectedDate.value = categoryMap[date]
    }

    // ✅ 내부 selectedDate 기준 추가 함수
    fun addCategory(category: Category) {
        val date = _selectedDate.value ?: return
        addCategory(date, category)
    }

    fun updateCategory(old: Category, new: Category) {
        val date = _selectedDate.value ?: return
        updateCategory(date, old, new)
    }

    fun deleteCategory(category: Category) {
        val date = _selectedDate.value ?: return
        deleteCategory(date, category)
    }

    // ✅ 날짜 지정 가능한 오버로드 함수들
    fun addCategory(date: LocalDate, category: Category) {
        val list = categoryMap.getOrPut(date) { defaultCategories.map { it.copy() }.toMutableList() }
        list.add(category)
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = list.toList()
        }
    }

    fun updateCategory(date: LocalDate, old: Category, new: Category) {
        val list = categoryMap[date] ?: return
        val index = list.indexOfFirst { it.id == old.id }
        if (index != -1) {
            list[index] = new
            if (_selectedDate.value == date) {
                _categoriesForSelectedDate.value = list.toList()
            }
        }
    }

    fun deleteCategory(date: LocalDate, category: Category) {
        val list = categoryMap[date] ?: return
        list.removeIf { it.id == category.id }
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = list.toList()
        }
    }

    fun removeTask(date: LocalDate, task: Task) {
        val list = categoryMap[date] ?: return
        for (category in list) {
            val tasks = category.tasksByDate[date]
            if (tasks != null && tasks.contains(task)) {
                val updatedTasks = tasks.toMutableList().apply { remove(task) }
                category.tasksByDate[date] = updatedTasks
            }
        }
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = list.toList()
        }
    }
}
