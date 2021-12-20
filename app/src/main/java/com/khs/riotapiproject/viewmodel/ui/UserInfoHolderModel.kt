package com.khs.riotapiproject.viewmodel.ui

class UserInfoHolderModel(val iconURL: String,
                          val name: String,
                          val soloRankTier: String,
                          val level: Int,
                          val rank: Int, val wins: Int, val losses: Int) {

    fun levelFormat(): String {
        return "Lv. $level"
    }

    fun rankFormat(): String {
        return "${rank}위"
    }

    fun winRatioFormat(): String {
        val winRatio = (wins * 100) / (wins + losses)
        return "승률 : ${winRatio}%"
    }
}