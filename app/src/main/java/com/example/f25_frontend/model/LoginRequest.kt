package com.example.f25_frontend.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
/*
    @Author 조수연
    피그마 기반 워크플로우 참고하여 Data class 정의
    백엔드 데이터베이스 설계도 완성 시 DTO 수정 예정
*/
@kotlinx.serialization.Serializable
data class LoginRequest(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String?,
    @SerializedName("deviceToken") val deviceToken: String?=null
) : Serializable