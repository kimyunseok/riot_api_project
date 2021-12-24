package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.R
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserInfoViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _userInfoLiveData = MutableLiveData<UserInfo>()
    val userInfoLiveData: LiveData<UserInfo>
        get() = _userInfoLiveData

    private val _levelFormat = MutableLiveData<String>()
    val levelFormat: LiveData<String>
        get() = _levelFormat

    private val _soloRankWinRatio = MutableLiveData<String>()
    val soloRankWinRatio: LiveData<String>
        get() = _soloRankWinRatio
    private val _freeRankWinRatio = MutableLiveData<String>()
    val freeRankWinRatio: LiveData<String>
        get() = _freeRankWinRatio

    private val _soloRankTierFormat = MutableLiveData("랭크 정보가 없습니다.")
    val soloRankTierFormat: LiveData<String>
        get() = _soloRankTierFormat
    private val _freeRankTierFormat = MutableLiveData("랭크 정보가 없습니다.")
    val freeRankTierFormat: LiveData<String>
        get() = _freeRankTierFormat

    private val _soloRankTierImage = MutableLiveData(R.drawable.emblem_iron)
    val soloRankTierImage: LiveData<Int>
        get() = _soloRankTierImage
    private val _freeRankTierImage = MutableLiveData(R.drawable.emblem_iron)
    val freeRankTierImage: LiveData<Int>
        get() = _freeRankTierImage

    private val _errorCodeLiveData = MutableLiveData<Int>()
    val errorCodeLiveData: LiveData<Int>
        get() = _errorCodeLiveData

    fun getUserInfoBySummonerName(summonerName: String) {
        val checkMinTimeForGetSummonerData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("get${summonerName}Info", 0) > 120000

        if(checkMinTimeForGetSummonerData) {
            CoroutineScope(Dispatchers.IO).launch {
                // 기본 유저 정보
                myRepository.getSummonerInfoByName(summonerName).let { summonerInfoResponse ->

                    Log.d("UserInfoViewModel", "GET User Info By Summoner Name code : ${summonerInfoResponse.code()}, message : ${summonerInfoResponse.message()}")

                    if(summonerInfoResponse.code() == 200) {
                        // 유저의 리그 정보
                        summonerInfoResponse.body()?.let { summonerInfo ->
                            myRepository.getSummonerLeagueInfoById(summonerInfo.id)
                                .let { leagueInfoResponse ->
                                    Log.d("UserInfoViewModel", "GET League Info By Summoner Name code : ${leagueInfoResponse.code()}, message : ${leagueInfoResponse.message()}")

                                    leagueInfoResponse.body()?.let { leagueInfo ->
                                        myRepository.deleteUserInfoBySummonerName(summonerName)

                                        val userInfo = when {
                                            leagueInfo.isEmpty() || leagueInfo[0].tier == null -> {
                                                UserInfo(
                                                    0,
                                                    summonerInfo.id,
                                                    summonerInfo.profileIconId,
                                                    summonerName,
                                                    summonerInfo.summonerLevel,
                                                    "NO_TIER",
                                                    "",
                                                    0,
                                                    0,
                                                    0,
                                                    "NO_TIER",
                                                    "",
                                                    0,
                                                    0,
                                                    0,
                                                )
                                            }
                                            leagueInfo.size == 1 || leagueInfo[1].tier == null -> {
                                                UserInfo(
                                                    0,
                                                    summonerInfo.id,
                                                    summonerInfo.profileIconId,
                                                    summonerName,
                                                    summonerInfo.summonerLevel,
                                                    leagueInfo[0].tier.toString(),
                                                    leagueInfo[0].rank.toString(),
                                                    leagueInfo[0].leaguePoints,
                                                    leagueInfo[0].wins,
                                                    leagueInfo[0].losses,
                                                    "NO_TIER",
                                                    "",
                                                    0,
                                                    0,
                                                    0,
                                                )
                                            }
                                            else -> {
                                                UserInfo(
                                                    0,
                                                    summonerInfo.id,
                                                    summonerInfo.profileIconId,
                                                    summonerName,
                                                    summonerInfo.summonerLevel,
                                                    leagueInfo[0].tier.toString(),
                                                    leagueInfo[0].rank.toString(),
                                                    leagueInfo[0].leaguePoints,
                                                    leagueInfo[0].wins,
                                                    leagueInfo[0].losses,
                                                    leagueInfo[1].tier.toString(),
                                                    leagueInfo[1].rank.toString(),
                                                    leagueInfo[1].leaguePoints,
                                                    leagueInfo[1].wins,
                                                    leagueInfo[1].losses
                                                )
                                            }
                                        }

                                        if(leagueInfo.isNotEmpty() && leagueInfo[0].tier != null) {
                                            setSoloRankTierFormat(
                                                leagueInfo[0].tier.toString(),
                                                leagueInfo[0].rank.toString(),
                                                leagueInfo[0].leaguePoints,
                                                leagueInfo[0].wins,
                                                leagueInfo[0].losses
                                            )
                                        }

                                        if(leagueInfo.size > 1 && leagueInfo[1].tier != null) {
                                            setFreeRankTierFormat(
                                                leagueInfo[1].tier.toString(),
                                                leagueInfo[1].rank.toString(),
                                                leagueInfo[1].leaguePoints,
                                                leagueInfo[1].wins,
                                                leagueInfo[1].losses
                                            )
                                        }

                                        GlobalApplication.mySharedPreferences.setLong("get${summonerName}Info", System.currentTimeMillis())

                                        setSummonerLevelFormat(summonerInfo.summonerLevel)
                                        myRepository.insertUserInfo(userInfo)
                                        _userInfoLiveData.postValue(userInfo)
                                    }
                                }
                        }
                    } else {
                        // 200번 아니라면 에러발생. 검색 실패.
                        _errorCodeLiveData.postValue(summonerInfoResponse.code())
                    }

                }
            }
        }
    }

    fun getUserInfoAtLocalDB(summonerName: String) {
        val userInfo = myRepository.getUserInfoBySummonerName(summonerName)

        userInfo?.let {

            if(it.soloRankTier != "NO_TIER") {
                setSoloRankTierFormat(
                    it.soloRankTier,
                    it.soloRankStage,
                    it.soloRankPoint,
                    it.soloRankWins,
                    it.soloRankLosses
                )
            }
            if(it.freeRankTier != "NO_TIER") {
                setFreeRankTierFormat(
                    it.freeRankTier,
                    it.freeRankStage,
                    it.freeRankPoint,
                    it.freeRankWins,
                    it.freeRankLosses
                )
            }

            _userInfoLiveData.postValue(it)
            setSummonerLevelFormat(it.userLevel)
        }
    }

    private fun setSoloRankTierFormat(tier: String, stage: String, point: Int, wins: Int, losses: Int) {
        val winRatio = (wins * 100) / (wins + losses)
        _soloRankWinRatio.postValue("${wins}승 ${losses}패\n승률 : ${winRatio}%")
        _soloRankTierFormat.postValue("$tier $stage $point")
        _soloRankTierImage.postValue(getRankTierImage(tier))
    }

    private fun setFreeRankTierFormat(tier: String, stage: String, point: Int, wins: Int, losses: Int) {
        val winRatio = (wins * 100) / (wins + losses)
        _freeRankWinRatio.postValue("${wins}승 ${losses}패\n승률 : ${winRatio}%")
        _freeRankTierFormat.postValue("$tier $stage $point")
        _freeRankTierImage.postValue(getRankTierImage(tier))
    }

    private fun getRankTierImage(tier: String): Int {
        return when(tier) {
            "CHALLENGER" -> {
                R.drawable.emblem_challenger
            }
            "GRANDMASTER" -> {
                R.drawable.emblem_grandmaster
            }
            "MASTER" -> {
                R.drawable.emblem_master
            }
            "DIAMOND" -> {
                R.drawable.emblem_diamond
            }
            "PLATINUM" -> {
                R.drawable.emblem_platinum
            }
            "GOLD" -> {
                R.drawable.emblem_gold
            }
            "SILVER" -> {
                R.drawable.emblem_silver
            }
            "BRONZE" -> {
                R.drawable.emblem_bronze
            }
            else -> {
                R.drawable.emblem_iron
            }
        }
    }

    private fun setSummonerLevelFormat(level: Int) {
        _levelFormat.postValue("LV. $level")
    }
}