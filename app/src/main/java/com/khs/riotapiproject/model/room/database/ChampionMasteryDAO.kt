package com.khs.riotapiproject.model.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.khs.riotapiproject.model.room.data.ChampionMastery

@Dao
interface ChampionMasteryDAO {
    @Insert
    fun insertChampionMastery(championMastery: ChampionMastery)

    @Query("SELECT * FROM ChampionMastery WHERE summonerName = :summonerName")
    fun getChampionMasteryListBySummonerName(summonerName: String): List<ChampionMastery>

    @Query("DELETE FROM ChampionMastery WHERE summonerID = :summonerID")
    fun clearChampionMasteryByID(summonerID: String)
}