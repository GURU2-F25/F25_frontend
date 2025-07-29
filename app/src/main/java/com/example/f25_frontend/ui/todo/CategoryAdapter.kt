package com.example.f25_frontend.ui.todo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.R
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import java.time.LocalDate

class CategoryAdapter(
    private var categories: List<Category>,
    private var selectedDate: LocalDate,
    private val onAddTaskClick: (Category) -> Unit,
    private val onTaskChecked: (Task) -> Unit,
    private val onTaskDeleted: (Task) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
        val tvAddTask: TextView = view.findViewById(R.id.tvAddTask)
        val taskRecyclerView: RecyclerView = view.findViewById(R.id.rvTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category.name
        holder.tvCategoryName.setTextColor(category.color)

        val tasks = category.tasksByDate[selectedDate] ?: emptyList()

        val taskAdapter = TaskAdapter(
            tasks = tasks,
            onTaskChecked = { task ->
                task.isDone = !task.isDone
                onTaskChecked(task)
            },
            onTaskDeleted = { task ->
                onTaskDeleted(task)
            }
        )

        holder.taskRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        holder.taskRecyclerView.adapter = taskAdapter

        // "+" 텍스트 눌러서 할 일 추가
        holder.tvAddTask.setOnClickListener {
            onAddTaskClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<Category>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }

    fun updateSelectedDate(date: LocalDate) {
        this.selectedDate = date
        notifyDataSetChanged()
    }
}
