package com.example.f25_frontend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import java.time.LocalDate

class CategoryAdapter(
    private val categories: List<Category>,
    private val onAddClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val btnAdd: ImageButton = itemView.findViewById(R.id.btnAddSchedule)
        val rvTasks: RecyclerView = itemView.findViewById(R.id.recyclerTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvName.text = category.name

        // 카테고리별 TaskAdapter 설정
        val taskAdapter = TaskAdapter(category.tasks)
        holder.rvTasks.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvTasks.adapter = taskAdapter

        // 플러스 버튼 → 해당 카테고리에만 Task 추가
        holder.btnAdd.setOnClickListener {
            onAddClick(category)   // 다이얼로그 띄워서 선택된 카테고리에 일정 추가
        }
    }

    override fun getItemCount(): Int = categories.size
}
