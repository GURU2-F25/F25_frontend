package com.example.f25_frontend.utils

import com.example.f25_frontend.model.CategoryDto
import com.example.f25_frontend.model.LoginRequest
import com.example.f25_frontend.model.TaskDto
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

interface RetrofitUtil {
//    회원가입
    @POST("/api/auth/join")
    fun join(
        @Body userDto:UserDto
    ): Call<UserDto>

//    @Todo 회원정보 수정 엔드포인트 추가 시 주석 해제
//    회원수정
    /*@PUT("/api/users/{id}")
    fun update(
        @Body userDto:UserDto
    ): Call<UserDto>*/

//    로그인
    @POST("/api/auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<UserDto>

//    유저 조회
    @GET("/api/users/{id}")
    fun getUser(
        @Path("id") id:String
    ): Call<JsonElement>

//    아이디 중복 검사
    @GET("/api/users/{id}/exists")
    fun isAvailableId(
        @Path("id") id:String
    ): Call<JsonElement>

//   유저 검색
   @GET("/api/search")
   fun searchUser(
       @Query("prefix") id:String
   ): Call<List<UserDto>>

//   내 디바이스 토큰 업데이트
   @PUT("/api/me/device-token")
   fun updateMyDeviceToken(
       @Query("deviceToken") deviceToken:String
   ): Call<JsonElement>
//   @TODO 회원 탈퇴
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
//   @TODO 해당 유저 친구목록 조회
   @GET("/api/{id}/friends")
   fun getFriends(
       @Path("id") id:String
   ): Call<List<UserDto>>
//   @TODO 해당 유저 팔로잉 목록 조회
   @GET("/api/{id}/following")
   fun getFollowing(
       @Path("id") id: String
   ): Call<List<UserDto>>
//   @TODO 해당 유저 팔로워 목록 조회
   @GET("/api/{id}/followers")
   fun getFollowers(
       @Path("id") id: String
   ): Call<List<UserDto>>
//   일정 목록 조회
   @GET("/api/todo/me")
   fun getMyTasks(
       @Query("date") date: String
   ): Call<List<TaskDto>>
//   타인 일정 목록 조회
   @GET("/api/todo/{id}")
   fun getTasks(
       @Path("id") id: String, @Query("date") date: String
   ): Call<List<TaskDto>>
//   새 일정 추가
   @POST("/api/todos")
   fun addTasks(
       @Body taskDto:TaskDto
   ): Call<JsonElement>
//   @TODO 그룹 일정 조회
   @GET("/api/todos/shared")
   fun getSharedTasks(
       @Query("date") date:String, @Query("user_id") id:String
   ): Call<JsonElement>
//    @TODO 타인 카테고리 조회 화면 &&& 스크롤뷰로 변경
//   해당 id 카테고리 정보 조회
   @GET("/api/category/{id}")
   fun getCategory(
       @Path("id") id:String
   ): Call<List<CategoryDto>>
//   내 카테고리 추가
   @POST("/api/category")
   fun addCategory(
       @Body categoryDto: CategoryDto
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

    //    @TODO 타인 일정 조회 화면 &&& 스크롤뷰로 변경
   @GET("/api/todo/{id}")
   fun getTodo(
       @Path("id") id: String, @Query("date") date: String
    ): Call<List<TaskDto>>

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
