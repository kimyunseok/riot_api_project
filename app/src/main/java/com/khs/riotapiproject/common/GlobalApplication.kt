package com.khs.riotapiproject.common

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
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

        //Glide URL -> ImageView 데이터바인딩에서 사용하기 위한 메서드
        @BindingAdapter("imageFromUrl")
        @JvmStatic
        fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
            if(!imageUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .thumbnail(Glide.with(view.context).load(CircularProgressDrawable(view.context)))
                    .into(view)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        mySharedPreferences = MySharedPreferences(applicationContext)
        setUpRetrofit()
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