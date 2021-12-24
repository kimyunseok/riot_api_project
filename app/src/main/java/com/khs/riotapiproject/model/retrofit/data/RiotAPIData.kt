package com.khs.riotapiproject.model.retrofit.data

import com.google.gson.annotations.SerializedName

// 랭킹 유저 정보 클래스
data class RankingData (
    var code: Int,
    var message: String,
    @SerializedName("tier")
    val tier: String,
    @SerializedName("leagueId")
    val leagueId: String,
    @SerializedName("queue")
    val queue: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("entries")
    val entries: MutableList<RankingDataDetail>
) {
    data class RankingDataDetail(
        @SerializedName("summonerId")
        val summonerId: String,
        @SerializedName("summonerName")
        val summonerName: String,
        @SerializedName("leaguePoints")
        val leaguePoints: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("wins")
        val wins: Int,
        @SerializedName("losses")
        val losses: Int,
        @SerializedName("veteran")
        val veteran: Boolean,
        @SerializedName("inactive")
        val inactive: Boolean,
        @SerializedName("freshBlood")
        val freshBlood: Boolean,
        @SerializedName("hotStreak")
        val hotStreak: Boolean
    )
}

// 소환사 정보 클래스(By SummonerID)
data class SummonerInfoData(
    var code: Int,
    var message: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("accountId")
    val accountId: String,
    @SerializedName("puuid")
    val puuid: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profileIconId")
    val profileIconId: Int,
    @SerializedName("revisionDate")
    val revisionDate: Long,
    @SerializedName("summonerLevel")
    val summonerLevel: Int
)

// 로테이션 챔피언 정보
data class RotationChampionData(
    var code: Int,
    var message: String,
    @SerializedName("freeChampionIds")
    val freeChampionIds: List<Int>,
    @SerializedName("freeChampionIdsForNewPlayers")
    val freeChampionIdsForNewPlayers: List<Int>,
    @SerializedName("maxNewPlayerLevel")
    val maxNewPlayerLevel: Int
)

data class SummonerLeagueInfoDetailData(
    @SerializedName("leagueId")
    val leagueId: String,
    @SerializedName("queueType")
    val queueType: String,
    @SerializedName("tier")
    val tier: String?,
    @SerializedName("rank")
    val rank: String?,
    @SerializedName("summonerId")
    val summonerId: String,
    @SerializedName("summonerName")
    val summonerName: String,
    @SerializedName("leaguePoints")
    val leaguePoints: Int,
    @SerializedName("wins")
    val wins: Int,
    @SerializedName("losses")
    val losses: Int,
)