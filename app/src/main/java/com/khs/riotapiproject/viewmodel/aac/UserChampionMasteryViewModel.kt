package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.common.GlobalApplication.Companion.minTimeForRequest
import com.khs.riotapiproject.model.room.data.ChampionMastery
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionMasteryHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserChampionMasteryViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _mostChampionListLiveData = MutableLiveData<List<ChampionMasteryHolderModel>>()
    val mostChampionListLiveData: LiveData<List<ChampionMasteryHolderModel>>
        get() = _mostChampionListLiveData

    fun getChampionMasteryFromServerBySummonerName(summonerName: String) {
        val checkMinTimeForGetChampionMastery =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong(
                "get${summonerName}ChampionMastery",
                0
            ) > minTimeForRequest

        if (checkMinTimeForGetChampionMastery) {
            CoroutineScope(Dispatchers.IO).launch {
                myRepository.getSummonerInfoByName(summonerName).let { summonerInfoResponse ->

                    Log.d(
                        "ChamMasteryViewModel",
                        "GET User Info By Summoner Name code : ${summonerInfoResponse.code()}, message : ${summonerInfoResponse.message()}"
                    )

                    if (summonerInfoResponse.code() == 200) {
                        // 유저의 리그 정보
                        summonerInfoResponse.body()?.let { summonerInfo ->
                            myRepository.getSummonerChampionMasteryById(summonerInfo.id).let { response ->
                                Log.d(
                                    "ChamMasteryViewModel",
                                    "GET User Champion Mastery By Summoner Name code : ${response.code()}, message : ${response.message()}"
                                )
                                if (response.code() == 200) {
                                    myRepository.clearChampionMasteryByID(summonerInfo.id)

                                    response.body()?.let { championListFromServer ->

                                        val maxLength = if (championListFromServer.size > 5) {
                                            5
                                        } else {
                                            championListFromServer.size
                                        }

                                        val mostChampionList = mutableListOf<ChampionMasteryHolderModel>()

                                        for (idx in 0 until maxLength) {
                                            val champion =
                                                myRepository.getChampionInfoByChampionKey(
                                                    championListFromServer[idx].championId.toString()
                                                )

                                            val championMastery = ChampionMastery(
                                                0,
                                                summonerInfo.id,
                                                summonerName,
                                                champion.championId,
                                                champion.championKey,
                                                champion.championName,
                                                championListFromServer[idx].championPoints,
                                                GlobalApplication.longToDateFormat(
                                                    championListFromServer[idx].lastPlayTime
                                                )
                                            )
                                            mostChampionList.add(ChampionMasteryHolderModel(championMastery))

                                            myRepository.insertChampionMastery(championMastery)
                                        }

                                        _mostChampionListLiveData.postValue(mostChampionList)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }


    fun getChampionMasteryFromLocalDBSummonerName(summonerName: String) {
        val mostChampionList = myRepository.getChampionMasteryListBySummonerName(summonerName)
        if (mostChampionList.isNotEmpty()) {
            val mostChampionHolderList = mutableListOf<ChampionMasteryHolderModel>()
            for(data in mostChampionList) {
                mostChampionHolderList.add(ChampionMasteryHolderModel(data))
            }

            _mostChampionListLiveData.postValue(mostChampionHolderList)
        }
    }
}