package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.common.GlobalApplication.Companion.minTimeForRequest
import com.khs.riotapiproject.model.retrofit.data.RankingData
import com.khs.riotapiproject.model.room.data.UserRankingInfo
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.UserRankingHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class UserSoloRankTop10ViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _soloRankTop10AtLocalDBListLiveData = MutableLiveData<List<UserRankingHolderModel>>()
    val soloRankTop10AtLocalDBListLiveData: LiveData<List<UserRankingHolderModel>>
        get() = _soloRankTop10AtLocalDBListLiveData

    private val _soloRankTop10ListFromServerLiveData = MutableLiveData<List<UserRankingHolderModel>>()
    val soloRankTop10ListFromServerLiveData: LiveData<List<UserRankingHolderModel>>
        get() = _soloRankTop10ListFromServerLiveData

    fun getRankingDataFromServer() {
        val checkMinTimeForGetRankingData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRankingDataTime", 0) > minTimeForRequest

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

                            setTop10Rank(rankList)
                            GlobalApplication.mySharedPreferences.setLong("getRankingDataTime", System.currentTimeMillis())
                        }

                    }
                } catch (e: ConnectException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getRankingDataFromLocalDB() {
        myRepository.getAllUserRankingInfo().let {
            val rankList = mutableListOf<UserRankingHolderModel>()
            for(userInfo in it) {
                rankList.add(UserRankingHolderModel(userInfo))
            }
            if(rankList.isNotEmpty()) {
                rankList.sortWith(compareBy { userModel -> userModel.userRankingInfo.rank })
                _soloRankTop10AtLocalDBListLiveData.postValue(rankList)
            }
        }
    }

    fun deleteUserInfoAtLocalDB(summonerID: String) {
        myRepository.deleteUserRankingInfo(summonerID)
    }

    fun saveUserInfoAtLocalDB(userRankingInfo: UserRankingInfo) {
        myRepository.insertUserRankingInfo(userRankingInfo)
    }

    private suspend fun setTop10Rank(rankList: List<RankingData.RankingDataDetail>) {
        val holderModelList = mutableListOf<UserRankingHolderModel>()

        val maxLength = if (rankList.size > 10) {
            10
        } else {
            rankList.size
        }


        for (idx in 0 until maxLength) {
            myRepository.getSummonerInfoById(rankList[idx].summonerId)
                .let { response ->

                    Log.d(
                        "UserInfoViewModel",
                        "Get Ranking User Info API. code : ${response.code()}, message : ${response.message()}"
                    )

                    response.body()?.let {
                        it.code = response.code()
                        it.message = response.message()

                        val userInfo = UserRankingInfo(
                            0,
                            it.id,
                            it.profileIconId,
                            it.name,
                            "CHALLENGER",
                            "",
                            "${rankList[idx].leaguePoints}",
                            it.summonerLevel,
                            idx + 1,
                            rankList[idx].wins,
                            rankList[idx].losses
                        )

                        holderModelList.add(UserRankingHolderModel(userInfo))
                    }
                }
        }

        _soloRankTop10ListFromServerLiveData.postValue(holderModelList)
    }

}