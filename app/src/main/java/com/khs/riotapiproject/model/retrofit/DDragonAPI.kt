package com.khs.riotapiproject.model.retrofit

import retrofit2.http.GET

interface DDragonAPI {
    @GET("cdn/11.24.1/data/ko_KR/champion.json")
    fun getAllChampionData()
}