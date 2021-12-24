package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.model.retrofit.data.ChampionData
import com.khs.riotapiproject.model.room.data.ChampionInfo
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.ConnectException

class ChampionInfoViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _championInfoLiveData = MutableLiveData<ChampionInfo>()
    val championInfoLiveData: LiveData<ChampionInfo>
        get() = _championInfoLiveData

    private val _allChampionListLiveData = MutableLiveData<List<ChampionInfo>>()
    val allChampionListLiveData: LiveData<List<ChampionInfo>>
        get() = _allChampionListLiveData

    private val _setRotationChampionListCompleteLiveData = MutableLiveData<Boolean>()
    val setRotationChampionListCompleteLiveData: LiveData<Boolean>
        get() = _setRotationChampionListCompleteLiveData

    private val _rotationChampionList = MutableLiveData<List<ChampionIconHolderModel>>()
    val rotationChampionList: LiveData<List<ChampionIconHolderModel>>
        get() = _rotationChampionList

    fun getChampionInfo(championKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val championInfo = myRepository.getChampionInfoByChampionKey(championKey)
            _championInfoLiveData.postValue(championInfo)
        }
    }

    fun setRotationListFromServer(version: String) {
        val checkMinTimeForGetRotationChampionData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("setRotationListTime", 0) > 120000

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

    fun getAllChampionData(version: String) {
        val checkVersionForGetChampionData =
            version != GlobalApplication.mySharedPreferences.getString("championDataVersion", "NO_VERSION")

        if (checkVersionForGetChampionData) {
            myRepository.clearChampionInfo()
            CoroutineScope(Dispatchers.IO).launch {
                myRepository.getAllChampionData(version).let {
                        response ->
                    Log.d("ChampionInfoViewModel", "Get All Champion DATA API. code : ${response.code()}, message : ${response.message()}")

                    response.body()?.let {
                        it.code = response.code()
                        it.message = response.message()

                        val allChampionDataJson = JSONObject(Gson().toJson(it.data))
                        val championList = ChampionData.Champion::class.java.declaredFields
                        val championListForPostValue = mutableListOf<ChampionInfo>()

                        for(champion in championList) {
                            val championName = champion.name
                            val championDataJson = allChampionDataJson.getJSONObject(championName)

                            val championInfo = ChampionInfo(
                                0,
                                championDataJson.get("version").toString(),
                                championDataJson.get("id").toString(),
                                championDataJson.get("key").toString(),
                                championDataJson.get("name").toString(),
                                championDataJson.get("title").toString(),
                                championDataJson.get("blurb").toString(),
                                false
                            )

                            championListForPostValue.add(championInfo)
                            myRepository.insertChampionInfo(championInfo)
                        }

                        _allChampionListLiveData.postValue(championListForPostValue)
                    }
                }
            }
        } else {
            val championList = myRepository.getAllChampionInfo()
            _allChampionListLiveData.postValue(championList)
        }
    }
}