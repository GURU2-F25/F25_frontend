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

    fun addCategory(category: Category) {
        val date = _selectedDate.value ?: return
        val list = categoryMap.getOrPut(date) { defaultCategories.map { it.copy() }.toMutableList() }
        list.add(category)
        _categoriesForSelectedDate.value = list.toList()
    }

    fun updateCategory(old: Category, new: Category) {
        val date = _selectedDate.value ?: return
        val list = categoryMap[date]
        val index = list?.indexOfFirst { it.id == old.id }
        if (index != null && index != -1) {
            list[index] = new
            _categoriesForSelectedDate.value = list.toList()
        }
    }

    fun deleteCategory(category: Category) {
        val date = _selectedDate.value ?: return
        val list = categoryMap[date]
        list?.removeIf { it.id == category.id }
        _categoriesForSelectedDate.value = list?.toList() ?: emptyList()
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
        _categoriesForSelectedDate.value = list.toList()
    }
}
