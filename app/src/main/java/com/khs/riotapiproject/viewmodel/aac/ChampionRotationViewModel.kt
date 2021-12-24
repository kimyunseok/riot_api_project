package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.common.GlobalApplication.Companion.minTimeForRequest
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class ChampionRotationViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _setRotationChampionListCompleteLiveData = MutableLiveData<Boolean>()
    val setRotationChampionListCompleteLiveData: LiveData<Boolean>
        get() = _setRotationChampionListCompleteLiveData

    private val _rotationChampionList = MutableLiveData<List<ChampionIconHolderModel>>()
    val rotationChampionList: LiveData<List<ChampionIconHolderModel>>
        get() = _rotationChampionList


    fun setRotationListFromServer(version: String) {
        val checkMinTimeForGetRotationChampionData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("setRotationListTime", 0) > minTimeForRequest

        val checkVersionForGetChampionData =
            version != GlobalApplication.mySharedPreferences.getString("championDataVersion", "NO_VERSION")

        if(checkVersionForGetChampionData || checkMinTimeForGetRotationChampionData) {
            myRepository.setAllChampionNoRotation()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    myRepository.getRotationChampionList().let {
                            response ->

                        Log.d("ChampionInfoViewModel", "Get Rotation Champion API. code : ${response.code()}, message : ${response.message()}")

                        response.body()?.let {
                            it.code = response.code()
                            it.message = response.message()

                            for(rotationChampionId in it.freeChampionIds) {
                                myRepository.setChampionRotation(rotationChampionId.toString())
                            }

                            for(rotationChampionIdForNew in it.freeChampionIdsForNewPlayers) {
                                myRepository.setChampionRotation(rotationChampionIdForNew.toString())
                            }
                        }

                        _setRotationChampionListCompleteLiveData.postValue(true)
                        GlobalApplication.mySharedPreferences.setString("championDataVersion", version)
                        GlobalApplication.mySharedPreferences.setLong("setRotationListTime", System.currentTimeMillis())
                    }
                } catch (e: ConnectException) {
                    e.printStackTrace()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            _setRotationChampionListCompleteLiveData.postValue(false)
        }
    }

    fun getRotationChampionData() {
        val rotationListFromDB = myRepository.getAllRotationChampionList()

        val rotationList = mutableListOf<ChampionIconHolderModel>()
        for (data in rotationListFromDB) {
            rotationList.add(ChampionIconHolderModel(data))
        }

        _rotationChampionList.postValue(rotationList)
    }

}