package com.example.f25_frontend.model

import java.time.LocalDate
/*
    @Author 김소연, 조수연
*/
data class TaskDto(
    val title: String,
    val date: LocalDate,      // 날짜별 일정 관리
    var isDone: Boolean = false,
    var categoryDto: CategoryDto
)
