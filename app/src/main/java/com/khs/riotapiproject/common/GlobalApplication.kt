package com.khs.riotapiproject.common

import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.khs.riotapiproject.R
import com.khs.riotapiproject.model.retrofit.InterceptorForHeader
import com.khs.riotapiproject.util.ImageSaveUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class GlobalApplication: Application() {
    companion object {
        lateinit var globalApplication: GlobalApplication
        lateinit var mySharedPreferences: MySharedPreferences
        lateinit var riotAPIService: Retrofit
        lateinit var dDragonAPIService: Retrofit

        val okHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(InterceptorForHeader())
            .build()

        //Glide URL -> ImageView 데이터바인딩에서 사용하기 위한 메서드
        @BindingAdapter("imageName", "imageURL", requireAll = true)
        @JvmStatic
        fun bindImageFromUrl(view: ImageView, imageName: String, imageURL: String) {
            if(ImageSaveUtil(view.context).checkAlreadySaved(imageName)) {
                // 저장돼 있는 이미지라면 캐시에서 불러온다.
                val fileName = "${imageName}.png"
                val cachePath = "${view.context.cacheDir}/file"
                val dir = File(cachePath)
                val fileItem = File("$dir/$fileName")

                Glide.with(view.context)
                    .load(fileItem)
                    .thumbnail(Glide.with(view.context).load(CircularProgressDrawable(view.context)))
                    .into(view)
            } else {
                // 저장 안된 이미지라면 웹에서 불러옴.
                val iconURL =  imageURL + "${imageName}.png"
                Glide.with(view.context)
                    .load(iconURL)
                    .thumbnail(Glide.with(view.context).load(CircularProgressDrawable(view.context)))
                    .into(view)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        globalApplication = this
        mySharedPreferences = MySharedPreferences(applicationContext)
        setDevelopmentAPIKey()
        setUpRiotAPIService()
        setUpDDragonAPIService()
    }

    private fun setDevelopmentAPIKey() {
        mySharedPreferences.setString("developmentAPIKey", applicationContext.getString(R.string.development_api_key))
    }

    private fun setUpRiotAPIService() {
        val riotURL = "https://kr.api.riotgames.com/"

        riotAPIService = Retrofit.Builder().baseUrl(riotURL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun setUpDDragonAPIService() {
        val dDragonURL = "https://ddragon.leagueoflegends.com/"

        dDragonAPIService = Retrofit.Builder().baseUrl(dDragonURL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}