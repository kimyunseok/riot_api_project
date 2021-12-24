package com.khs.riotapiproject.viewmodel.ui

import com.khs.riotapiproject.model.room.data.UserRankingInfo

class UserInfoHolderModel(val userRankingInfo: UserRankingInfo) {

    fun getTierFormat(): String {
        return "${userRankingInfo.soloRankTier} ${userRankingInfo.soloRankStage} ${userRankingInfo.soloRankPoint}점"
    }

    fun levelFormat(): String {
        return "Lv. ${userRankingInfo.level}"
    }

    fun rankFormat(): String {
        return "${userRankingInfo.rank}위"
    }

    fun winRatioFormat(): String {
        val winRatio = (userRankingInfo.soloRankWins * 100) / (userRankingInfo.soloRankWins + userRankingInfo.soloRankLosses)
        return "${userRankingInfo.soloRankWins}승 ${userRankingInfo.soloRankLosses}패\n승률 : ${winRatio}%"
    }

    fun getName(): String {
        return userRankingInfo.name
    }

    fun getIconID(): Int {
        return userRankingInfo.iconID
    }
}