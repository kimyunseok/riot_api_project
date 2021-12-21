package com.khs.riotapiproject.viewmodel.repository

import com.khs.riotapiproject.common.GlobalApplication.Companion.retrofitService
import com.khs.riotapiproject.model.retrofit.RiotAPI

class MainRepository {
    private val retrofit = retrofitService.create(RiotAPI::class.java)

    suspend fun getRanking() = retrofit.getRanking()
    suspend fun getSummonerInfoById(encryptedSummonerId: String) = retrofit.getSummonerInfoById(encryptedSummonerId = encryptedSummonerId)
    suspend fun getRotationChampionList() = retrofit.getRotationChampionList()
}