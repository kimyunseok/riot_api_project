package com.khs.riotapiproject.viewmodel.ui

import com.khs.riotapiproject.model.room.data.UserInfo

class UserInfoHolderModel(val userInfo: UserInfo) {

    fun getTierFormat(): String {
        return "${userInfo.soloRankTier} ${userInfo.soloRankStage} ${userInfo.soloRankPoint}점"
    }

    fun levelFormat(): String {
        return "Lv. ${userInfo.level}"
    }

    fun rankFormat(): String {
        return "${userInfo.rank}위"
    }

    fun winRatioFormat(): String {
        val winRatio = (userInfo.soloRankWins * 100) / (userInfo.soloRankWins + userInfo.soloRankLosses)
        return "${userInfo.soloRankWins}승 ${userInfo.soloRankLosses}패\n승률 : ${winRatio}%"
    }

    fun getName(): String {
        return userInfo.name
    }

    fun getIconID(): Int {
        return userInfo.iconID
    }
}