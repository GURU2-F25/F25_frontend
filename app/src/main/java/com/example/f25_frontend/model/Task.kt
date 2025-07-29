package com.example.f25_frontend.model

import java.time.LocalDate

data class Task(
    val title: String,
    val date: LocalDate,
    val category: Category,
    var isDone: Boolean
)
