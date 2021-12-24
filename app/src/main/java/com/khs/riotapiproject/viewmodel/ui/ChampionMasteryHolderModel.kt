package com.khs.riotapiproject.viewmodel.ui

import com.khs.riotapiproject.common.GlobalApplication.Companion.numberCommaFormat
import com.khs.riotapiproject.model.room.data.ChampionMastery

class ChampionMasteryHolderModel(val championMastery: ChampionMastery) {

    fun getMasteryFormat(): String {
        return "${numberCommaFormat(championMastery.championPoint.toString())}점"
    }

    fun getRecentPlayFormat(): String {
        return "최근 플레이 : ${championMastery.recentPlayTime}"
    }
}