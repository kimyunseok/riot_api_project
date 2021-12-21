package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.model.retrofit.data.RankingData
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val _rankingDataLiveData = MutableLiveData<RankingData>()
    val rankingDataLiveData: LiveData<RankingData>
        get() = _rankingDataLiveData
    private val rankingDataDetailListValue: MutableList<RankingData.RankingDataDetail>
        get() = rankingDataLiveData.value?.entries?: mutableListOf()

    private val _userInfoListLiveData = MutableLiveData<List<UserInfo>>()
    val userInfoListLiveData: LiveData<List<UserInfo>>
        get() = _userInfoListLiveData

    private val _rotationChampionListLiveData = MutableLiveData<List<UserInfoHolderModel>>()
    val rotationChampionListLiveData: LiveData<List<UserInfoHolderModel>>
        get() = rotationChampionListLiveData

    fun getRankingData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainRepository.getRanking().let {
                        response ->

                    Log.d("MainViewModel", "Get Ranking API. code : ${response.code()}, message : ${response.message()}")

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
                }
            } catch (e: ConnectException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUserInfoListByIds() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val rankList = mutableListOf<UserInfo>()

                val maxLength = if(rankingDataDetailListValue.size > 10) {
                    10
                } else {
                    rankingDataDetailListValue.size
                }

                for(idx in 0 until maxLength) {
                    mainRepository.getSummonerInfoById(rankingDataDetailListValue[idx].summonerId).let {
                            response ->

                        Log.d("MainViewModel", "Get User Info API. code : ${response.code()}, message : ${response.message()}")

                        response.body()?.let {
                            it.code = response.code()
                            it.message = response.message()

                            val userInfo = UserInfo(
                                0,
                                it.profileIconId,
                                it.name,
                                "챌린저 ${rankingDataDetailListValue[idx].leaguePoints}점",
                                it.summonerLevel,
                                idx + 1,
                                rankingDataDetailListValue[idx].wins,
                                rankingDataDetailListValue[idx].losses
                            )

                            rankList.add(userInfo)
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

    fun getRotationList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainRepository.getRotationChampionList().let {
                    response ->

                    Log.d("MainViewModel", "Get Rotation Champion API. code : ${response.code()}, message : ${response.message()}")

                    response.body()?.let {
                        it.code = response.code()
                        it.message = response.message()
                    }
                }
            } catch (e: ConnectException) {
                e.printStackTrace()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}