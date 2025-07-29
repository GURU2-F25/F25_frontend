package com.example.f25_frontend.utils

import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import com.example.f25_frontend.model.UserDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface retrofitUtil {
//    회원가입
    @POST("/api/users")
    fun join(
        @Body userDto:UserDto
    ): Call<UserDto>
//    로그인
    @POST("/api/user/login")
    fun login(
        @Body userDto:UserDto
    ): Call<UserDto>

    @GET("/api/users/{id}")

    suspend fun getUser(
        @Path("id") userId:String
    ): Call<UserDto>

//    @GET("/api/user")
//    fun getUser(
//        @Header("Authorization") auth : String,
//        @Query("query") query : String
//    ): Call<UserDto>
}
