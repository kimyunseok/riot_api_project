package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.common.GlobalApplication.Companion.mySharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class InterceptorForHeader: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder =chain.request().newBuilder().apply {
            addHeader("X-Riot-Token", mySharedPreferences.getString("developmentAPIKey", "NO_KEY"))
        }
        return chain.proceed(builder.build())
    }
}