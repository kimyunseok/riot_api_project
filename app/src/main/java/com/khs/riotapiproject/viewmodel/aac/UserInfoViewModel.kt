package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.model.retrofit.data.RankingData
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class UserInfoViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _rankingDataLiveData = MutableLiveData<RankingData>()
    val rankingDataLiveData: LiveData<RankingData>
        get() = _rankingDataLiveData
    private val rankingDataDetailListValue: MutableList<RankingData.RankingDataDetail>
        get() = rankingDataLiveData.value?.entries?: mutableListOf()

    private val _userInfoAtLocalDBListLiveData = MutableLiveData<List<UserInfoHolderModel>>()
    val userInfoAtLocalDBListLiveData: LiveData<List<UserInfoHolderModel>>
        get() = _userInfoAtLocalDBListLiveData

    private val _userInfoListLiveData = MutableLiveData<List<UserInfoHolderModel>>()
    val userInfoListLiveData: LiveData<List<UserInfoHolderModel>>
        get() = _userInfoListLiveData

    var rankingRoomDBLoad = false

    fun getRankingDataFromServer() {
        val checkMinTimeForGetRankingData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRankingDataTime", 0) > 120000

        if(checkMinTimeForGetRankingData) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    myRepository.getRanking().let {
                            response ->

                        Log.d("UserInfoViewModel", "Get Ranking API. code : ${response.code()}, message : ${response.message()}")

                        response.body()?.apply {
                            code = response.code()
                            message = response.message()
                        }

                        response.body()?.entries?.let {
                                rankList ->
                            rankList.sortWith(compareBy { it.leaguePoints })
                            rankList.reverse()
                        }
                        _rankingDataLiveData.postValue(response.body())
                        GlobalApplication.mySharedPreferences.setLong("getRankingDataTime", System.currentTimeMillis())
                    }
                } catch (e: ConnectException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getRankingUserInfoListByRankingDataFromServer() {
        val checkMinTimeForGetRankingUserInfo =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRankingUserInfoTime", 0) > 120000

        if(checkMinTimeForGetRankingUserInfo) {
            GlobalApplication.mySharedPreferences.setLong("getRankingUserInfoTime", System.currentTimeMillis())
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val rankList = mutableListOf<UserInfoHolderModel>()

                    val maxLength = if (rankingDataDetailListValue.size > 10) {
                        10
                    } else {
                        rankingDataDetailListValue.size
                    }

                    for (idx in 0 until maxLength) {
                        myRepository.getSummonerInfoById(rankingDataDetailListValue[idx].summonerId)
                            .let { response ->

                                Log.d(
                                    "UserInfoViewModel",
                                    "Get Ranking User Info API. code : ${response.code()}, message : ${response.message()}"
                                )

                                response.body()?.let {
                                    it.code = response.code()
                                    it.message = response.message()

                                    val userInfo = UserInfo(
                                        0,
                                        it.id,
                                        it.profileIconId,
                                        it.name,
                                        "챌린저 ${rankingDataDetailListValue[idx].leaguePoints}점",
                                        it.summonerLevel,
                                        idx + 1,
                                        rankingDataDetailListValue[idx].wins,
                                        rankingDataDetailListValue[idx].losses
                                    )

                                    rankList.add(UserInfoHolderModel(userInfo))
                                }
                            }
                    }

                    _userInfoListLiveData.postValue(rankList)
                } catch (e: ConnectException) {
                    e.printStackTrace()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getRankingDataFromLocalDB() {
        myRepository.getAllUserInfo().let {
            val rankList = mutableListOf<UserInfoHolderModel>()
            for(userInfo in it) {
                rankList.add(UserInfoHolderModel(userInfo))
            }
            if(rankList.isNotEmpty()) {
                rankingRoomDBLoad = true
            }
            rankList.sortWith(compareBy { userModel -> userModel.userInfo.rank })

            _userInfoAtLocalDBListLiveData.postValue(rankList)
        }
    }

    fun clearUserInfoAtLocalDB() {
        myRepository.clearUserInfo()
    }

    fun saveUserInfoAtLocalDB(userInfo: UserInfo) {
        myRepository.insertUserInfo(userInfo)
    }

}