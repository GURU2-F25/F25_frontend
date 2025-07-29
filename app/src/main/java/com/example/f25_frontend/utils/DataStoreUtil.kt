package com.example.f25_frontend.utils

import android.content.Context
import android.content.SharedPreferences

//안드로이드 데이터저장소 라이브러리 설정
class DataStoreUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

//    getter setter override, 편의성을 위해 defaultValue empty 메소드 추가 구현
    fun getString(key: String): String {
        return prefs.getString(key, "").toString()
    }
    fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue).toString()
    }
    fun setString(key: String, str: String?) {
        prefs.edit().putString(key, str).apply()
    }
}