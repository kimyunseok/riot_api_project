package com.khs.riotapiproject.viewmodel.repository

import com.khs.riotapiproject.common.GlobalApplication.Companion.dDragonAPIService
import com.khs.riotapiproject.common.GlobalApplication.Companion.riotAPIService
import com.khs.riotapiproject.model.retrofit.DDragonAPI
import com.khs.riotapiproject.model.retrofit.RiotAPI
import com.khs.riotapiproject.model.room.data.ChampionInfo
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.model.room.database.AppDataBase

class MyRepository {
    private val riotRetrofit = riotAPIService.create(RiotAPI::class.java)
    private val ddragonRetrofit = dDragonAPIService.create(DDragonAPI::class.java)

    private val userInfoDB = AppDataBase.roomDBInstance.userInfoDAO()
    private val championInfoDB = AppDataBase.roomDBInstance.championInfoDAO()

    // Retrofit2, Riot API 호출
    suspend fun getRanking() = riotRetrofit.getRanking()
    suspend fun getSummonerInfoById(encryptedSummonerId: String) = riotRetrofit.getSummonerInfoById(encryptedSummonerId = encryptedSummonerId)
    suspend fun getRotationChampionList() = riotRetrofit.getRotationChampionList()

    //Retrofit2, DDragon API 호출
    suspend fun getAllChampionData() = ddragonRetrofit.getAllChampionData()

    // User - SQLite
    fun getAllUserInfo() = userInfoDB.getAllUserInfo()
    fun clearUserInfo() = userInfoDB.clearUserInfo()
    fun insertUserInfo(userInfo: UserInfo) = userInfoDB.insertUserInfo(userInfo)

    // Champion - SQLite
    fun getAllChampionInfo() = championInfoDB.getAllChampionInfo()
    fun getAllRotationChampionList() = championInfoDB.getAllRotationChampionList()
    fun setAllChampionNoRotation() = championInfoDB.setAllChampionNoRotation()
    fun setChampionRotation(key: String) = championInfoDB.setChampionRotation(key)
    fun insertChampionInfo(championInfo: ChampionInfo) = championInfoDB.insertChampionInfo(championInfo)
    fun clearChampionInfo() = championInfoDB.clearChampionInfo()
    fun getChampionInfoByChampionKey(championKey: String) = championInfoDB.getChampionInfoByChampionKey(championKey)
}