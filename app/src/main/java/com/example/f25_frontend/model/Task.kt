package com.example.f25_frontend.model

import java.io.Serializable
import java.time.LocalDate

data class Task(
    val title: String,
    val date: LocalDate,
    var isDone: Boolean = false,
    var category: Category
) : Serializable
