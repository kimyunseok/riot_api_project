package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.model.retrofit.data.ChampionData
import retrofit2.Response
import retrofit2.http.GET

interface DDragonAPI {
    @GET("cdn/11.24.1/data/ko_KR/champion.json")
    suspend fun getAllChampionData(): Response<ChampionData>
}