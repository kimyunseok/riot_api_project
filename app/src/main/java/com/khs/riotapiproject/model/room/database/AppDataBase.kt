package com.khs.riotapiproject.model.room.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.model.room.data.ChampionInfo
import com.khs.riotapiproject.model.room.data.UserRankingInfo
import com.khs.riotapiproject.model.room.data.UserInfo

@Database(entities = [UserRankingInfo::class, ChampionInfo::class, UserInfo::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userRankingInfoDAO(): UserRankingInfoDAO
    abstract fun championInfoDAO(): ChampionInfoDAO
    abstract fun userLeagueInfoDAO(): UserInfoDAO

    companion object {
        val roomDBInstance = Room.databaseBuilder(
            GlobalApplication.globalApplication,
            AppDataBase::class.java, "RitoAPI.db"
        )
            .fallbackToDestructiveMigration() // 기존 데이터 버리고 다음 버전으로 넘어감.
            .allowMainThreadQueries() // 메인 스레드에서 DB 접근 가능.
            .build()
    }
}