package com.example.f25_frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.R
import com.example.f25_frontend.model.TaskDto
/*
    @Author 김소연
    일정 데이터 바인딩 어댑터
*/
class TaskAdapter(
    private val taskDtos: List<TaskDto>,
    private val onTaskChecked: (TaskDto) -> Unit,
    private val onTaskDeleted: (TaskDto) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbTask: CheckBox = itemView.findViewById(R.id.cbTask)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskDtos[position]

        holder.tvTitle.text = task.title

        // ✅ 중요: 이전 리스너 제거 (RecyclerView 재활용 방지)
        holder.cbTask.setOnCheckedChangeListener(null)

        // ✅ 체크 상태 설정
        holder.cbTask.isChecked = task.isDone

        // ✅ 체크 이벤트 리스너 등록
        holder.cbTask.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
            onTaskChecked(task)
        }

        holder.btnDelete.setOnClickListener {
            onTaskDeleted(task)
        }
    }

    override fun getItemCount(): Int = taskDtos.size
}
