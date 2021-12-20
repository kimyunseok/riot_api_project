package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.model.data.RankingData
import com.khs.riotapiproject.model.data.SummonerInfoData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RiotAPI {
    @GET("/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5")
    suspend fun getRanking(): Response<RankingData>

    @GET("/lol/summoner/v4/summoners/{encryptedSummonerId}")
    suspend fun getSummonerInfoById(@Path("encryptedSummonerId") encryptedSummonerId: String): Response<SummonerInfoData>
}