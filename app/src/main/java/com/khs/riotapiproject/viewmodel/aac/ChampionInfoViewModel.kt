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
import com.khs.riotapiproject.viewmodel.ui.RotationChampionHolderModel
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

    private val _rotationChampionListLiveData = MutableLiveData<List<RotationChampionHolderModel>>()
    val rotationChampionListLiveData: LiveData<List<RotationChampionHolderModel>>
        get() = _rotationChampionListLiveData

    fun getChampionInfo(championKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val championInfo = myRepository.getChampionInfoByChampionKey(championKey)
            _championInfoLiveData.postValue(championInfo)
        }
    }

    fun getRotationListFromServer() {
        val checkMinTimeForGetRotationChampionData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRotationChampionData", 0) > 120000

        if(checkMinTimeForGetRotationChampionData) {
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
                        val rotationListFromDB = myRepository.getAllRotationChampionList()
                        val rotationList = mutableListOf<RotationChampionHolderModel>()
                        for(data in rotationListFromDB) {
                            rotationList.add(RotationChampionHolderModel(data))
                        }

                        _rotationChampionListLiveData.postValue(rotationList)
                        GlobalApplication.mySharedPreferences.setLong("getRotationChampionData", System.currentTimeMillis())
                    }
                } catch (e: ConnectException) {
                    e.printStackTrace()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            // 만일 2분 전에 요청 시 캐싱된 데이터를 보여준다. 캐싱된 데이터가 비어있을 경우 다시 한번 메서드를 호출해서 서버와 통신해서 데이터를 받아온다.
            // 데이터가 빈 경우는 모든 챔프는 다시 불러왔지만(Rotation 처리가 다 풀림) RotationList를 받아오기에는 2분이 안돼서 불러오지 않으면 데이터가 빈다.
            val rotationListFromDB = myRepository.getAllRotationChampionList()
            if(rotationListFromDB.isNotEmpty()) {
                val rotationList = mutableListOf<RotationChampionHolderModel>()
                for (data in rotationListFromDB) {
                    rotationList.add(RotationChampionHolderModel(data))
                }
                _rotationChampionListLiveData.postValue(rotationList)
            } else {
                GlobalApplication.mySharedPreferences.setLong("getRotationChampionData", 0)
                getRotationListFromServer()
            }
        }
    }

    fun getAllChampionDataFromServer() {
        val checkMinTimeForGetAllChampionData =
            System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getAllChampionData", 0) > 120000

        if (checkMinTimeForGetAllChampionData) {
            myRepository.clearChampionInfo()
            CoroutineScope(Dispatchers.IO).launch {
                myRepository.getAllChampionData().let {
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
                        GlobalApplication.mySharedPreferences.setLong("getAllChampionData", System.currentTimeMillis())
                    }
                }
            }
        } else {
            val championList = myRepository.getAllChampionInfo()
            _allChampionListLiveData.postValue(championList)
        }

    }
}