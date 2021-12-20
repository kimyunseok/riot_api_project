package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.R
import com.khs.riotapiproject.model.data.RankingData
import com.khs.riotapiproject.model.data.SummonerInfoData
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

    private val _userInfoListLiveData = MutableLiveData<List<UserInfoHolderModel>>()
    val userInfoListLiveData: LiveData<List<UserInfoHolderModel>>
        get() = _userInfoListLiveData
    val userInfoListValue: List<UserInfoHolderModel>
        get() = userInfoListLiveData.value?: mutableListOf()

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
                val rankList = mutableListOf<UserInfoHolderModel>()

                val maxLength = if(rankingDataDetailListValue.size > 10) {
                    10
                } else {
                    rankingDataDetailListValue.size
                }

                for(idx in 0 until maxLength) {
                    mainRepository.getSummonerInfoById(rankingDataDetailListValue[idx].summonerId).let {
                            response ->

                        response.body()?.let {
                            it.code = response.code()
                            it.message = response.message()

                            // 최신버전 정보는 https://ddragon.leagueoflegends.com/api/versions.json 에서 확인가능.
                            val userInfoHolderModel = UserInfoHolderModel(
                                "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon/${it.profileIconId}.png",
                                it.name,
                                "챌린저 ${rankingDataDetailListValue[idx].leaguePoints}점",
                                it.summonerLevel,
                                idx + 1,
                                rankingDataDetailListValue[idx].wins,
                                rankingDataDetailListValue[idx].losses
                            )

                            rankList.add(userInfoHolderModel)
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