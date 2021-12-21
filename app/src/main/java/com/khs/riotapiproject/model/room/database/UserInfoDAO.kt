package com.khs.riotapiproject.model.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.khs.riotapiproject.model.room.data.UserInfo

@Dao
interface UserInfoDAO {
    @Insert
    suspend fun insertUserInfo(userInfo: UserInfo)

    @Query("DELETE FROM UserInfo")
    suspend fun clearUserInfo()
}