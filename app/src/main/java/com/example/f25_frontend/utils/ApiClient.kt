package com.example.f25_frontend.utils

import com.example.f25_frontend.MyApplication
import com.example.f25_frontend.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {

    private val realServerURL:String = "https://f25-backend.onrender.com/"
    private val devServerURL:String = "https://10.0.0.2:8000/"

//    val retrofit = Retrofit.Builder().baseUrl(realServerURL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    val service:retrofitUtil = retrofit.create(retrofitUtil::class.java)
    fun getNoAuthApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(realServerURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getAuthApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(realServerURL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
            = OkHttpClient.Builder().run {
        addInterceptor(interceptor)
        build()
    }
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("access_token", MyApplication.prefs.getString("access_token"))
                .addHeader("token_type", MyApplication.prefs.getString("token_type"))
                .build()
            proceed(newRequest)
        }
    }
}