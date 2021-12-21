package com.khs.riotapiproject.model.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = true) var userInfoID: Long,
    val iconID: Int,
    val name: String,
    val soloRankTier: String,
    val level: Int,
    val rank: Int,
    val wins: Int,
    val losses: Int
)