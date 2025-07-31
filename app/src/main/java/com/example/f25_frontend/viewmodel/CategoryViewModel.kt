package com.example.f25_frontend.viewmodel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.f25_frontend.model.CategoryDto
import com.example.f25_frontend.model.TaskDto
import java.time.LocalDate
/*
    @Author 김소연
*/
class CategoryViewModel : ViewModel() {

    private val categoryDtoMap = mutableMapOf<LocalDate, MutableList<CategoryDto>>()

    private val _selectedDate = MutableLiveData<LocalDate>(LocalDate.now())
    val selectedDate: LiveData<LocalDate> get() = _selectedDate

    private val _categoriesForSelectedDate = MutableLiveData<List<CategoryDto>>()
    val categoriesForSelectedDate: LiveData<List<CategoryDto>> get() = _categoriesForSelectedDate

    private val defaultCategories = listOf(
        CategoryDto(name = "과제(전공)", color = Color.parseColor("#FCDC58"), id="dev1", userId= "devId"),
        CategoryDto(name = "과제(교양)", color = Color.parseColor("#DCDC85"), id="dev2", userId= "devId"),
        CategoryDto(name = "자기계발", color = Color.parseColor("#9B9BFF"), id="dev3", userId= "devId"),
        CategoryDto(name = "운동", color = Color.parseColor("#A7D397"), id="dev4", userId = "devId"),
        CategoryDto(name = "취미", color = Color.parseColor("#FF9B9B"), id="dev5", userId = "devId")
    )

    init {
        val today = _selectedDate.value ?: LocalDate.now()
        categoryDtoMap[today] = defaultCategories.map { it.copy() }.toMutableList()
        _categoriesForSelectedDate.value = categoryDtoMap[today]
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        if (!categoryDtoMap.containsKey(date)) {
            categoryDtoMap[date] = defaultCategories.map { it.copy() }.toMutableList()
        }
        _categoriesForSelectedDate.value = categoryDtoMap[date]
    }

    fun addCategory(categoryDto: CategoryDto) {
        val date = _selectedDate.value ?: return
        addCategory(date, categoryDto)
    }

    fun updateCategory(old: CategoryDto, new: CategoryDto) {
        val date = _selectedDate.value ?: return
        updateCategory(date, old, new)
    }

    fun deleteCategory(categoryDto: CategoryDto) {
        val date = _selectedDate.value ?: return
        deleteCategory(date, categoryDto)
    }

    fun addCategory(date: LocalDate, categoryDto: CategoryDto) {
        val list = categoryDtoMap.getOrPut(date) { defaultCategories.map { it.copy() }.toMutableList() }
        list.add(categoryDto)
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = list.toList()
        }
    }

    fun updateCategory(date: LocalDate, old: CategoryDto, new: CategoryDto) {
        val list = categoryDtoMap[date] ?: return
        val index = list.indexOfFirst { it.id == old.id }
        if (index != -1) {
            list[index] = new
            if (_selectedDate.value == date) {
                _categoriesForSelectedDate.value = list.toList()
            }
        }
    }

    fun deleteCategory(date: LocalDate, categoryDto: CategoryDto) {
        val list = categoryDtoMap[date] ?: return
        list.removeIf { it.id == categoryDto.id }
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = list.toList()
        }
    }

    fun removeTask(date: LocalDate, taskDto: TaskDto) {
        val list = categoryDtoMap[date] ?: return
        for (category in list) {
            val tasks = category.tasksByDate[date]
            if (tasks != null && tasks.contains(taskDto)) {
                val updatedTasks = tasks.toMutableList().apply { remove(taskDto) }
                category.tasksByDate[date] = updatedTasks
            }
        }
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = list.toList()
        }
    }

    fun setCategoriesForSelectedDate(date: LocalDate, categories: List<CategoryDto>) {
        categoryDtoMap[date] = categories.toMutableList()
        if (_selectedDate.value == date) {
            _categoriesForSelectedDate.value = categories
        }
    }

    fun setCategoriesForSelectedDate(categories: List<CategoryDto>) {
        val date = _selectedDate.value ?: return
        setCategoriesForSelectedDate(date, categories)
    }
}
