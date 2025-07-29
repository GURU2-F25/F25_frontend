package com.example.f25_frontend.utils

import com.example.f25_frontend.model.UserDto
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface retrofitUtil {
//    회원가입
    @POST("/api/users")
    fun join(
        @Body userDto:UserDto
    ): Call<UserDto>

//    아이디 중복 검사
    @GET("/api/users/exists")
    fun exists(

    )
//    로그인
    @POST("/api/user/login")
    fun login(
        @Body userDto:UserDto
    ): Call<UserDto>

//    유저 조회
   @GET("/api/users/{id}")
   fun getUser(
        @Path(value = "id") id:String
    ): Call<JsonElement>

}
