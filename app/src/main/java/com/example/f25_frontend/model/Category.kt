package com.example.f25_frontend.model

import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: Int,
    val tasksByDate: MutableMap<LocalDate, MutableList<Task>> = mutableMapOf()
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
