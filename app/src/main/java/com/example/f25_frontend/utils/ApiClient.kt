package com.example.f25_frontend.utils

import com.example.f25_frontend.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
/*
    @Author 조수연
    서버 통신을 위한 RETROFIT2 라이브러리 유틸리티
    Bearer 보안 헤더, 디버깅 모듈 인터셉터
*/
object ApiClient {
    private val REAL_SERVER_URL:String = "https://f25-backend.onrender.com/"
//    private val DEV_SERVER_URL:String = "https://10.0.0.2:8000/"

//  로그인 전 Bearer Authorization 토큰이 없을 때 백엔드 통신 메소드
    fun getNoAuthApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(REAL_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
//  Bearer Authorization 토큰을 인터셉터를 활용하여 자동 추가한 후 백엔드 통신 메소드
    fun getAuthApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(REAL_SERVER_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
//  RETROFIT2 통신 모듈에 디버깅을 위해 로그 인터셉터와 헤더 자동 추가 인터셉터 설정
    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
                addInterceptor(HttpLoggingInterceptor().let{it.setLevel(HttpLoggingInterceptor.Level.BODY)})
                addInterceptor(interceptor)
                build()
    }
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer "+MyApplication.prefs.getString("access_token"))
                .addHeader("token_type", MyApplication.prefs.getString("token_type"))
                .build()
            proceed(newRequest)
        }
    }
}