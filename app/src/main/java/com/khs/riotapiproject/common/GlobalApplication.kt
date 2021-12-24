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
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GlobalApplication: Application() {
    companion object {
        lateinit var globalApplication: GlobalApplication
        lateinit var mySharedPreferences: MySharedPreferences
        lateinit var riotAPIService: Retrofit
        lateinit var dDragonAPIService: Retrofit

        const val minTimeForRequest = 120000 // 1000(1초) * 60(1분) * 2, 2분마다 한번씩 같은 데이터 요청 가능.

        val okHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(InterceptorForHeader())
            .build()

        //Glide URL -> ImageView 데이터바인딩에서 사용하기 위한 메서드
        @BindingAdapter("imageName", "imageURL", "imageType", requireAll = true)
        @JvmStatic
        fun bindImageFromUrl(view: ImageView, imageName: String, imageURL: String, imageType: String) {
            val iconURL =  imageURL + "${imageName}.${imageType}"
            if(ImageSaveUtil(view.context).checkAlreadySaved(imageName, imageType)) {
                // 저장돼 있는 이미지라면 캐시에서 미리보기
                val fileName = "${imageName}.${imageType}"
                val cachePath = "${view.context.cacheDir}/file"
                val dir = File(cachePath)
                val fileItem = File("$dir/$fileName")
                Glide.with(view.context)
                    .load(iconURL)
                    .thumbnail(Glide.with(view.context).load(fileItem))
                    .into(view)
            } else {
                // 저장 안된 이미지라면 로딩창
                Glide.with(view.context)
                    .load(iconURL)
                    .thumbnail(Glide.with(view.context).load(CircularProgressDrawable(view.context)))
                    .into(view)
            }
        }

        fun numberCommaFormat(number: String): String {
            val decimalFormat = DecimalFormat("#,###")
            return decimalFormat.format(Integer.valueOf(number))
        }

        fun longToDateFormat(time: Long): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
            return dateFormat.format(time)
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