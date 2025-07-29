package com.example.f25_frontend

import android.app.Application
import com.example.f25_frontend.utils.DataStoreUtil

//앱 실행 시 Preference 저장소 우선 생성
class MyApplication : Application() {
    companion object {
        lateinit var prefs: DataStoreUtil
    }

    override fun onCreate() {
        prefs = DataStoreUtil(applicationContext)
        super.onCreate()
    }
}