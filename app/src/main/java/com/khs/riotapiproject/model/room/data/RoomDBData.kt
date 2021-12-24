package com.khs.riotapiproject.model.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserRankingInfo(
    @PrimaryKey(autoGenerate = true) var userInfoID: Long,
    val summonerID: String,
    val iconID: Int,
    val name: String,
    val soloRankTier: String,
    val soloRankStage: String,
    val soloRankPoint: String,
    val level: Int,
    val rank: Int,
    val soloRankWins: Int,
    val soloRankLosses: Int
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

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = true) var userInfoID: Long,
    val summonerID: String,
    val iconID: Int,
    val userName: String,
    val userLevel: Int,
    val soloRankTier: String,
    val soloRankStage: String,
    val soloRankPoint: Int,
    val soloRankWins: Int,
    val soloRankLosses: Int,
    val freeRankTier: String,
    val freeRankStage: String,
    val freeRankPoint: Int,
    val freeRankWins: Int,
    val freeRankLosses: Int
)

@Entity
data class ChampionMastery(
    @PrimaryKey(autoGenerate = true) var championMasteryID: Long,
    val summonerID: String,
    val summonerName: String,
    val championID: String,
    val championKey: String,
    val championName: String,
    val championPoint: Int,
    val recentPlayTime: String,
)