package com.khs.riotapiproject.model.retrofit.data

import com.google.gson.annotations.SerializedName

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
    val entries: List<RankingDataDetail>,
) {
    data class RankingDataDetail(
        @SerializedName("summonerId")
        val summonerId: String,
        @SerializedName("summonerName")
        val summonerName: String,
        @SerializedName("leaguePoints")
        val leaguePoints: String,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("wins")
        val wins: String,
        @SerializedName("losses")
        val losses: String,
        @SerializedName("veteran")
        val veteran: String,
        @SerializedName("inactive")
        val inactive: String,
        @SerializedName("freshBlood")
        val freshBlood: String,
        @SerializedName("hotStreak")
        val hotStreak: String,
    )
}