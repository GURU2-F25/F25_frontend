package com.example.f25_frontend.utils

import android.content.Context
import android.content.SharedPreferences

class DataStoreUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

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