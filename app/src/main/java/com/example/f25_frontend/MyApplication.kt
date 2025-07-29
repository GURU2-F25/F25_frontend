package com.example.f25_frontend

import android.app.Application
import com.example.f25_frontend.utils.DataStoreUtil

//앱 실행 시 Preference 저장소 최우선 생성이 필요하여 Application Override
class MyApplication : Application() {
    companion object {
        lateinit var prefs: DataStoreUtil
    }
    override fun onCreate() {
        prefs = DataStoreUtil(applicationContext)
        super.onCreate()
    }
}