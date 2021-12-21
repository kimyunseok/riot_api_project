package com.khs.riotapiproject.viewmodel.ui

import com.khs.riotapiproject.model.room.data.UserInfo

class UserInfoHolderModel(val userInfo: UserInfo) {

    fun levelFormat(): String {
        return "Lv. ${userInfo.level}"
    }

    fun rankFormat(): String {
        return "${userInfo.rank}위"
    }

    fun winRatioFormat(): String {
        val winRatio = (userInfo.wins * 100) / (userInfo.wins + userInfo.losses)
        return "${userInfo.wins}승 ${userInfo.losses}패\n승률 : ${winRatio}%"
    }

    fun getName(): String {
        return userInfo.name
    }

    fun getSoloRankTier(): String {
        return userInfo.soloRankTier
    }

    fun getIconID(): Int {
        return userInfo.iconID
    }
}