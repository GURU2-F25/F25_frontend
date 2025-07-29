package com.example.f25_frontend.utils

import com.example.f25_frontend.model.Category
import com.example.f25_frontend.model.Task
import com.example.f25_frontend.model.UserDto
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface retrofitUtil {
//    회원가입
    @POST("/api/auth/join")
    fun join(
        @Body userDto:UserDto
    ): Call<UserDto>
//    아이디 중복 검사
    @GET("/api/users/{id}/exists")
    fun exists(
    )
//    로그인
    @POST("/api/auth/login")
    fun login(
        @Body userDto:UserDto
    ): Call<UserDto>
//    유저 조회
   @GET("/api/users/{id}")
   fun getUser(
        @Path("id") id:String
    ): Call<JsonElement>
//   유저 검색
   @GET("/api/search")
   fun searchUser(
       @Query("prefix") id:String
   ): Call<List<UserDto>>
//   내 친구 목록 조회
   @GET("/api/me/friends")
   fun getMyFriends(
   ): Call<List<UserDto>>
//   내 팔로잉 목록
   @GET("/api/me/following")
   fun getMyFollowing(
   ): Call<List<UserDto>>
//   내 팔로워 목록
   @GET("/api/me/followers")
   fun getMyFollowers(
   ): Call<List<UserDto>>
//   내 디바이스 토큰 업데이트
   @PUT("/api/me/device-token")
   fun updateMyDeviceToken(
       @Query("deviceToken") deviceToken:String
   ): Call<JsonElement>
//   회원 탈퇴
   @DELETE("/api/me")
   fun deleteMyAccount(
       @Body password:String
   ): Call<JsonElement>
//   타 사용자 팔로우
   @POST("/api/follow/{target_id}")
   fun follow(
       @Path("target_id") targetId: String
   ): Call<JsonElement>
//   타 사용자 언팔로우
   @DELETE("/api/follow/{target_id}")
   fun unFollow(
       @Path("target_id") targetId: String
   ): Call<JsonElement>
//   해당 유저 친구목록 조회
   @GET("/api/{id}/friends")
   fun getFriends(
       @Path("id") id:String
   ): Call<List<UserDto>>
//   해당 유저 팔로잉 목록 조회
   @GET("/api/{id}/following")
   fun getFollowing(
       @Path("id") id: String
   ): Call<List<UserDto>>
//   해당 유저 팔로워 목록 조회
   @GET("/api/{id}/followers")
   fun getFollowers(
       @Path("id") id: String
   ): Call<List<UserDto>>
//   TODOS TASKS CHECK MODIFY
//   일정 목록 조회
   @GET("/api/todo")
   fun getMyTasks(
       @Query("date") date: String
   ): Call<List<Task>>
//   타인 일정 목록 조회
   @GET("/api/todo/{id}")
   fun getTasks(
       @Path("id") id: String
   ): Call<List<Task>>
//   새 일정 추가
   @POST("/api/todos")
   fun addTasks(
       @Body task:Task
   ): Call<JsonElement>
//   그룹 일정 조회
   @GET("/api/todos/shared")
   fun getSharedTasks(
       @Query("date") date:String, @Query("user_id") id:String
   ): Call<JsonElement>
//   해당 id 카테고리 정보 조회
   @GET("/api/categories")
   fun getCategories(
       @Query("id") id:String
   ): Call<List<Category>>
//   내 카테고리 추가
   @POST("/api/categories")
   fun addCategory(
       @Body category: Category
   ): Call<JsonElement>
//   내 카테고리 수정
   @PUT("/api/categories/{category_id}")
   fun updateCategory(
       @Path("category_id") categoryId: String
   ): Call<JsonElement>
//   내 카테고리 삭제
   @DELETE("/api/categories/{category_id}")
   fun deleteCategory(
       @Path("category_id") categoryId: String
   ): Call<JsonElement>
//   내 일정 수정
   @PUT("/api/todos/{todo_id}")
   fun updateTodo(
       @Path("todo_id") todoId: String
   ): Call<JsonElement>
//   내 일정 삭제
   @DELETE("/api/todos/{todo_id}")
   fun deleteTodo(
       @Path("todo_id") todoId: String
   ): Call<JsonElement>
//   내 일정 정보 변경(완료)
   @PATCH("/api/todos/{todo_id}/check")
   fun patchTodo(
       @Path("todo_id") todoId: String
   ): Call<JsonElement>
}
