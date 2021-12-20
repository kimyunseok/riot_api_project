package com.khs.riotapiproject.common

import android.app.Application
import android.util.Log
import com.khs.riotapiproject.R
import com.khs.riotapiproject.model.retrofit.InterceptorForHeader
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GlobalApplication: Application() {
    companion object {
        lateinit var mySharedPreferences: MySharedPreferences
        lateinit var retrofitService: Retrofit
    }

    override fun onCreate() {
        super.onCreate()
        mySharedPreferences = MySharedPreferences(applicationContext)
        setDevelopmentAPIKey()
        setUpRetrofit()
    }

    private fun setDevelopmentAPIKey() {
        mySharedPreferences.setString("developmentAPIKey", applicationContext.getString(R.string.development_api_key))
    }

    private fun setUpRetrofit() {
        val baseURL = "https://kr.api.riotgames.com/"

        val okHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(InterceptorForHeader())
            .build()

        retrofitService = Retrofit.Builder().baseUrl(baseURL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}