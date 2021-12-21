package com.khs.riotapiproject.model.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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

@Entity
data class ChampionInfo(
    @PrimaryKey(autoGenerate = true) var championInfoID: Long,
    val version: String,
    val championId: String,
    val championKey: String,
    val championName: String,
    val championTitle: String,
    val championDescription: String,
    var isRotation: Boolean
)