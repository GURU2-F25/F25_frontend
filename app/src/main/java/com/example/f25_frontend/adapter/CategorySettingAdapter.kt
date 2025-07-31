package com.example.f25_frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.R
import com.example.f25_frontend.model.CategoryDto
/*
    @Author 김소연
    일정 카테고리 변경 데이터 바인딩 어댑터
*/
class CategorySettingAdapter(
    private val categories: MutableList<CategoryDto>,
    private val onEditClick: (CategoryDto) -> Unit,
    private val onDeleteClick: (CategoryDto) -> Unit
) : RecyclerView.Adapter<CategorySettingAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val btnEditCategory: Button = itemView.findViewById(R.id.btnEditCategory)
        val btnDeleteCategory: Button = itemView.findViewById(R.id.btnDeleteCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_setting, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category.name

        holder.btnEditCategory.setOnClickListener {
            onEditClick(category)
        }

        holder.btnDeleteCategory.setOnClickListener {
            onDeleteClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newList: List<CategoryDto>) {
        categories.clear()
        categories.addAll(newList)
        notifyDataSetChanged()
    }

}
