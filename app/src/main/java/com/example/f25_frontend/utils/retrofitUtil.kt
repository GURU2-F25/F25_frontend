package com.example.f25_frontend.utils

import com.example.f25_frontend.model.User
import retrofit2.Retrofit

interface retrofitUtil {
/*
    @POST("/api/user")
    fun joinUser(
//        @Query("status") apiKey: String
    ) : Call<User>

//    BACKEND if(pw!=null&&id!=null --> 로그인 확인하는거네
//    {
//        return 데이터 가공해서 보내주고 statusCode 어 일치하는거 있다 비번까지 OK
//        우리는 받아서 어 유저정보 넘어왓네 비번 맞네 ? 비밀번호가 일치하지 않습니다 / 아이디가 일치하지 않습니다
//        이렇ㄱ레 구분 안되어있고 아이디 혹은 비밀번호가 일치하지 않습니다.
//    })
//    if(login)
    @GET("/api/user")
    fun getUser(
//        @Query(id=paramId, pw=paramPw).callBack().if(parameter id!=null && pw!=null){
//
//        }
//        if(id!=null && pw == null ){
//            중복검사 목적이네
//        })
    )

    @GET("/api/todo")
    fun getTodo(

    )
    @DELETE("/api/todo")
    fun deleteTodo(

    )
    @PUT("/api/todo")
    fun updateTodo(
//        if(complete==true){
//            backEnd --> db.getMySubscribeUser() 날 구독하고 있는 유저들 목록 가져오기{
//                가져온 유저들의 고유 휴대폰 값(firebase)들에 전체 push 알람 쏘기
//                        그리고 투두는 완료로 변경
//            }
//            그리고 임박한 스케쥴 푸시 알림 보내주겠다? 이건 백엔드쪽에서 스케쥴러 구현해서 돌려야함.
//                    scheduler(예시 : 10분주기로 투두리스트에서 마감일정과 현재시간의 차이가 N분 이하일 경우 리스트업){
//                        사람들에게 firebase push 알람 쏘기
    )

// 시간 없으니 팔로워 수락/거부 기능 넣지 말고 인스타 등의 SNS처럼 일방적 팔로우를 통해 상대방의 일정을 알 수 있게 하되,
//    유저마다 내 일정 (전체공개 / 맞팔공개) 설정하게끔 해주는게 작업 효율면에서 편할듯(필터링만 주면 되니까)
    @GET("/api/follow")
    fun getFollow(
//        해당 유저의 팔로워들 데이터 리스트 가져오기(각 유저들은 내 일정 전체공개, 맞팔공개 설정 보유)
//        userId 파라미터를 통해 팔로워 리스트 찾기
    )

    @POST("/api/follow")
    fun addFollow(
//        해당 유저를 팔로우하기
//        만약 상대 유저를 내가 팔로우 했을 경우, 상대 유저에게 PUSH 알림 보내고 해당 팝업 클릭 시 상대방 유저 검색 페이지로 포워딩
    )

    @DELETE("/api/follow")
    fun deleteFollow(
//        해당 유저 언팔하기
    )

 */
}