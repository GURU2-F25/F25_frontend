package com.example.f25_frontend.utils

import com.example.f25_frontend.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient {
    private val REAL_SERVER_URL:String = "https://f25-backend.onrender.com/"
//    private val DEV_SERVER_URL:String = "https://10.0.0.2:8000/"

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
//    ADD HTTP LOGGER INTERCEPTOR && AUTHORIZATION INTERCEPTOR
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