package com.example.f25_frontend.ui.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.f25_frontend.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class WeekAdapter(
    private var days: List<LocalDate>,
    private val totalWidth: Int,
    private val onDateClick: (LocalDate) -> Unit
) : RecyclerView.Adapter<WeekAdapter.DayViewHolder>() {

    private var selectedDate: LocalDate = LocalDate.now()

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay = itemView.findViewById<TextView>(R.id.tvDayOfWeek)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDayNumber)

        fun bind(date: LocalDate) {
            val dayText = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
            val dayNumber = date.dayOfMonth.toString()

            tvDay.text = dayText
            tvDate.text = dayNumber

            if (date == selectedDate) {
                tvDate.setBackgroundResource(R.drawable.bg_selected_date)
                tvDate.setTypeface(null, Typeface.BOLD)
            } else {
                tvDate.background = null
                tvDate.setTypeface(null, Typeface.NORMAL)
            }

            itemView.setOnClickListener {
                selectedDate = date
                notifyDataSetChanged()
                onDateClick(date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.width = totalWidth / 7
        view.layoutParams = layoutParams
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    fun updateDays(newDays: List<LocalDate>) {
        days = newDays
        notifyDataSetChanged()
    }

    fun updateSelectedDate(newDate: LocalDate) {
        selectedDate = newDate
        notifyDataSetChanged()
    }


}
