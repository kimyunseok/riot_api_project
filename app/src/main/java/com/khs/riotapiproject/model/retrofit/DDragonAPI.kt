package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.model.retrofit.data.ChampionData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DDragonAPI {
    @GET("cdn/{version}/data/ko_KR/champion.json")
    suspend fun getAllChampionData(@Path("version")version: String): Response<ChampionData>
}