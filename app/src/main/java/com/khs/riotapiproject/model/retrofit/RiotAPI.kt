package com.khs.riotapiproject.model.retrofit

import com.khs.riotapiproject.model.retrofit.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RiotAPI {
    @GET("/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5")
    suspend fun getRanking(): Response<RankingData>

    @GET("/lol/summoner/v4/summoners/{encryptedSummonerId}")
    suspend fun getSummonerInfoById(@Path("encryptedSummonerId") encryptedSummonerId: String): Response<SummonerInfoData>

    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}")
    suspend fun getSummonerInfoByName(@Path("summonerName") summonerName: String): Response<SummonerInfoData>

    @GET("/lol/platform/v3/champion-rotations")
    suspend fun getRotationChampionList(): Response<RotationChampionData>

    @GET("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun getSummonerLeagueInfoById(@Path("encryptedSummonerId") encryptedSummonerId: String): Response<List<SummonerLeagueInfoDetailData>>

    @GET("/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}")
    suspend fun getSummonerChampionMasteryById(@Path("encryptedSummonerId") encryptedSummonerId: String): Response<List<SummonerChampionMasteryData>>

}