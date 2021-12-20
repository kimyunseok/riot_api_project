package com.khs.riotapiproject.model.data

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
    val entries: MutableList<RankingDataDetail>,
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
        val hotStreak: Boolean,
    )
}