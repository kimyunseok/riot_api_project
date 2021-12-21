package com.khs.riotapiproject.common

import android.content.Context
import android.content.SharedPreferences
import com.khs.riotapiproject.R

class MySharedPreferences(context: Context) {
    private val mySharedPreferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun getString(key: String, defaultValue: String): String {
        return mySharedPreferences.getString(key, defaultValue).toString()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return mySharedPreferences.getLong(key, defaultValue)
    }

    fun setString(key: String, value: String) {
        mySharedPreferences.edit().putString(key, value).apply()
    }

    fun setLong(key: String, value: Long) {
        mySharedPreferences.edit().putLong(key, value).apply()
    }
}