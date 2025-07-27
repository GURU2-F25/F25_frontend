package com.example.f25_frontend.model

import com.google.gson.annotations.SerializedName

//firebase push notification add.

data class User(
//    @SerializedName("uniqueId") val uniqueId: String,
    @SerializedName("id") val id: String,
    @SerializedName("password") val pw: String,
    @SerializedName("userName") val nickname: String,
    @SerializedName("profileImage") val profileImage: String,
)