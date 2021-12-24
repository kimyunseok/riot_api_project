package com.khs.riotapiproject.viewmodel.repository

import com.khs.riotapiproject.common.GlobalApplication.Companion.dDragonAPIService
import com.khs.riotapiproject.common.GlobalApplication.Companion.riotAPIService
import com.khs.riotapiproject.model.retrofit.DDragonAPI
import com.khs.riotapiproject.model.retrofit.RiotAPI
import com.khs.riotapiproject.model.room.data.ChampionInfo
import com.khs.riotapiproject.model.room.data.ChampionMastery
import com.khs.riotapiproject.model.room.data.UserRankingInfo
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.model.room.database.AppDataBase

class MyRepository {
    private val riotRetrofit = riotAPIService.create(RiotAPI::class.java)
    private val ddragonRetrofit = dDragonAPIService.create(DDragonAPI::class.java)

    private val userInfoDB = AppDataBase.roomDBInstance.userRankingInfoDAO()
    private val userLeagueInfoDB = AppDataBase.roomDBInstance.userLeagueInfoDAO()
    private val championInfoDB = AppDataBase.roomDBInstance.championInfoDAO()
    private val championMasteryDB = AppDataBase.roomDBInstance.championMasteryDAO()

    // Retrofit2, Riot API 호출
    suspend fun getRanking() = riotRetrofit.getRanking()
    suspend fun getSummonerInfoById(encryptedSummonerId: String) = riotRetrofit.getSummonerInfoById(encryptedSummonerId)
    suspend fun getSummonerInfoByName(summonerName: String) = riotRetrofit.getSummonerInfoByName(summonerName)
    suspend fun getRotationChampionList() = riotRetrofit.getRotationChampionList()
    suspend fun getSummonerLeagueInfoById(encryptedSummonerId: String) = riotRetrofit.getSummonerLeagueInfoById(encryptedSummonerId)
    suspend fun getSummonerChampionMasteryById(encryptedSummonerId: String) = riotRetrofit.getSummonerChampionMasteryById(encryptedSummonerId)

    //Retrofit2, DDragon API 호출
    suspend fun getAllChampionData(version: String) = ddragonRetrofit.getAllChampionData(version)

    // User Ranking - SQLite
    fun getAllUserRankingInfo() = userInfoDB.getAllUserRankingInfo()
    fun deleteUserRankingInfo(summonerID: String) = userInfoDB.deleteUserRankingInfo(summonerID)
    fun insertUserRankingInfo(userRankingInfo: UserRankingInfo) = userInfoDB.insertUserRankingInfo(userRankingInfo)

    // User Info - SQLite
    fun insertUserInfo(userInfo: UserInfo) = userLeagueInfoDB.insertUserInfo(userInfo)
    fun getUserInfoBySummonerName(summonerName: String) = userLeagueInfoDB.getUserInfoBySummonerName(summonerName)
    fun deleteUserInfoBySummonerName(summonerName: String) = userLeagueInfoDB.deleteUserInfoBySummonerName(summonerName)

    // Champion - SQLite
    fun getAllChampionInfo() = championInfoDB.getAllChampionInfo()
    fun getAllRotationChampionList() = championInfoDB.getAllRotationChampionList()
    fun setAllChampionNoRotation() = championInfoDB.setAllChampionNoRotation()
    fun setChampionRotation(key: String) = championInfoDB.setChampionRotation(key)
    fun insertChampionInfo(championInfo: ChampionInfo) = championInfoDB.insertChampionInfo(championInfo)
    fun clearChampionInfo() = championInfoDB.clearChampionInfo()
    fun getChampionInfoByChampionKey(championKey: String) = championInfoDB.getChampionInfoByChampionKey(championKey)

    // Champion Mastery - SQLite
    fun insertChampionMastery(championMastery: ChampionMastery) = championMasteryDB.insertChampionMastery(championMastery)
    fun getChampionMasteryListBySummonerName(summonerName: String) = championMasteryDB.getChampionMasteryListBySummonerName(summonerName)
    fun clearChampionMasteryByID(summonerID: String) = championMasteryDB.clearChampionMasteryByID(summonerID)
}