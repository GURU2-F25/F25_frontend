package com.example.f25_frontend.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("password") val password: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("profileImage") val profileImage: String,
    @SerializedName("uid") val uId: String,
    @SerializedName("access_token") val access_token : String,
    @SerializedName("token_type") val token_type : String,
//    @SerializedName("follower") val follower: List<String>?,
//    @SerializedName("following") val following: List<String>?,
//    @SerializedName("task") val task: List<Task>?,
//    @SerializedName("deviceToken") val deviceToken: String?
)