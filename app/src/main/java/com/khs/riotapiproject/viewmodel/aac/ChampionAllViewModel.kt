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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ChampionAllViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _allChampionListLiveData = MutableLiveData<List<ChampionInfo>>()
    val allChampionListLiveData: LiveData<List<ChampionInfo>>
        get() = _allChampionListLiveData

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