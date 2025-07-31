package com.example.f25_frontend.utils

import android.content.Context
import android.content.SharedPreferences
/*
    @Author 조수연
    안드로이드 SharedPreferences 내부 데이터베이스 유틸리티 구현
*/
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