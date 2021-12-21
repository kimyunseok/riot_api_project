package com.khs.riotapiproject.model.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.khs.riotapiproject.model.room.data.ChampionInfo

@Dao
interface ChampionInfoDAO {
    @Insert
    fun insertChampionInfo(championInfo: ChampionInfo)

    @Query("SELECT * FROM ChampionInfo")
    fun getAllChampionInfo(): List<ChampionInfo>

    @Query("DELETE FROM ChampionInfo")
    fun clearChampionInfo()

    @Query("UPDATE ChampionInfo SET isRotation = :isRotation")
    fun setAllChampionNoRotation(isRotation: Boolean = false)

    @Query("UPDATE ChampionInfo SET isRotation = :isRotation WHERE championKey = :championKey")
    fun setChampionRotation(championKey: String, isRotation: Boolean = true)

    @Query("SELECT * FROM ChampionInfo WHERE isRotation = :isRotation")
    fun getAllRotationChampionList(isRotation: Boolean = true): List<ChampionInfo>
}