package com.example.f25_frontend.model

import java.time.LocalDate

data class Task(
    val title: String,
    val date: LocalDate,      // 날짜별 일정 관리
    var isDone: Boolean = false
)
