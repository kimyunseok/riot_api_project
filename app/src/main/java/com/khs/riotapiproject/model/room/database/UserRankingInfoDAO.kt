package com.khs.riotapiproject.model.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.khs.riotapiproject.model.room.data.UserRankingInfo

@Dao
interface UserRankingInfoDAO {
    @Insert
    fun insertUserRankingInfo(userRankingInfo: UserRankingInfo)

    @Query("SELECT * FROM UserRankingInfo")
    fun getAllUserRankingInfo(): List<UserRankingInfo>

    @Query("DELETE FROM UserRankingInfo WHERE summonerID = :summonerID")
    fun deleteUserRankingInfo(summonerID: String)
}