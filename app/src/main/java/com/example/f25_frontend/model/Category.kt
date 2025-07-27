package com.example.f25_frontend.model

data class Category(
    val name: String,
    val tasks: MutableList<Task> = mutableListOf()   // 카테고리별 일정 저장
)
