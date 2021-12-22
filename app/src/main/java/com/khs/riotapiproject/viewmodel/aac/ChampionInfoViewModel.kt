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
    val allChampionListValue: List<ChampionInfo>
        get() = allChampionListLiveData.value?: mutableListOf()

    private val _rotationChampionListLiveData = MutableLiveData<List<RotationChampionHolderModel>>()
    val rotationChampionListLiveData: LiveData<List<RotationChampionHolderModel>>
        get() = _rotationChampionListLiveData

    fun getChampionInfo(championKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val championInfo = myRepository.getChampionInfoByChampionKey(championKey)
            _championInfoLiveData.postValue(championInfo)
        }
    }

    val checkMinTimeForGetRotationChampionData: Boolean by lazy {
        System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRotationChampionData", 0) > 120000
    }

    fun getRotationList() {
        if(checkMinTimeForGetRotationChampionData) {
            myRepository.setAllChampionNoRotation()
            GlobalApplication.mySharedPreferences.setLong("getRotationChampionData", System.currentTimeMillis())

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
                        }
                        val rotationListFromDB = myRepository.getAllRotationChampionList()
                        val rotationList = mutableListOf<RotationChampionHolderModel>()
                        for(data in rotationListFromDB) {
                            rotationList.add(RotationChampionHolderModel(data))
                        }

                        _rotationChampionListLiveData.postValue(rotationList)
                    }
                } catch (e: ConnectException) {
                    e.printStackTrace()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val rotationListFromDB = myRepository.getAllRotationChampionList()
                val rotationList = mutableListOf<RotationChampionHolderModel>()
                for(data in rotationListFromDB) {
                    rotationList.add(RotationChampionHolderModel(data))
                }
                _rotationChampionListLiveData.postValue(rotationList)
            }
        }
    }

    fun getAllChampionData(version: String) {
        val versionChanged = (version != GlobalApplication.mySharedPreferences.getString("lolVersion", "NO_VERSION"))

        if (versionChanged) {
            myRepository.clearChampionInfo()
            CoroutineScope(Dispatchers.IO).launch {
                GlobalApplication.mySharedPreferences.setString("lolVersion", version)
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
                    }
                }
            }
        } else {
            val championList = myRepository.getAllChampionInfo()
            _allChampionListLiveData.postValue(championList)
        }

    }
}