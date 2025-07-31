package com.example.f25_frontend.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
/*
    @Author 김소연, 조수연
*/
data class CategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: Int,
    @SerializedName("tasksByDate") var tasksByDate: MutableMap<LocalDate, MutableList<TaskDto>> = mutableMapOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryDto
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
