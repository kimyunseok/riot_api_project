package com.khs.riotapiproject.model.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.khs.riotapiproject.model.room.data.UserInfo

@Dao
interface UserInfoDAO {
    @Insert
    fun insertUserInfo(userInfo: UserInfo)

    @Query("SELECT * FROM UserInfo WHERE userName = :userName")
    fun getUserInfoBySummonerName(userName: String): UserInfo?

    @Query("DELETE FROM UserInfo WHERE userName = :userName")
    fun deleteUserInfoBySummonerName(userName: String)
}