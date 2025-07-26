package com.example.f25_frontend

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.core.WeekDay

class WeekDayContainer(view: View) : ViewContainer(view) {
    val dayText: TextView = view.findViewById(R.id.tvDayNumber)
    val weekDayText: TextView = view.findViewById(R.id.tvWeekDay)
    lateinit var day: WeekDay
}
