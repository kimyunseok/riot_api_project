package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.model.retrofit.data.RankingData
import retrofit2.Response
import retrofit2.http.GET

interface RiotAPI {
    @GET("/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5")
    suspend fun getRanking(): Response<RankingData>
}