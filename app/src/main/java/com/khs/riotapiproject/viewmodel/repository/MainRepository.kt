package com.khs.riotapiproject.viewmodel.repository

import com.khs.riotapiproject.common.GlobalApplication.Companion.riotAPIService
import com.khs.riotapiproject.model.retrofit.RiotAPI
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.model.room.database.AppDataBase

class MainRepository {
    private val retrofit = riotAPIService.create(RiotAPI::class.java)

    private val userInfoDB = AppDataBase.roomDBInstance.userInfoDAO()

    // Retrofit2, Riot API 호출
    suspend fun getRanking() = retrofit.getRanking()
    suspend fun getSummonerInfoById(encryptedSummonerId: String) = retrofit.getSummonerInfoById(encryptedSummonerId = encryptedSummonerId)
    suspend fun getRotationChampionList() = retrofit.getRotationChampionList()

    // SQLite
    fun getAllUserInfo() = userInfoDB.getAllUserInfo()
    suspend fun clearUserInfo() = userInfoDB.clearUserInfo()
    suspend fun insertUserInfo(userInfo: UserInfo) = userInfoDB.insertUserInfo(userInfo)
}