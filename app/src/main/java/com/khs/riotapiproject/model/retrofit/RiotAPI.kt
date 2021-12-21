package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.model.retrofit.data.RankingData
import com.khs.riotapiproject.model.retrofit.data.RotationChampionData
import com.khs.riotapiproject.model.retrofit.data.SummonerInfoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RiotAPI {
    @GET("/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5")
    suspend fun getRanking(): Response<RankingData>

    @GET("/lol/summoner/v4/summoners/{encryptedSummonerId}")
    suspend fun getSummonerInfoById(@Path("encryptedSummonerId") encryptedSummonerId: String): Response<SummonerInfoData>

    @GET("/lol/platform/v3/champion-rotations")
    suspend fun getRotationChampionList(): Response<RotationChampionData>
}