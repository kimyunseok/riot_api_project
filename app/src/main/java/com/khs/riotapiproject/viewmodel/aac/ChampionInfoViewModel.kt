package com.khs.riotapiproject.viewmodel.aac

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.model.room.data.ChampionInfo
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChampionInfoViewModel(private val myRepository: MyRepository): ViewModel() {
    private val _championInfoLiveData = MutableLiveData<ChampionInfo>()
    val championInfoLiveData: LiveData<ChampionInfo>
        get() = _championInfoLiveData

    fun getChampionInfo(championKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val championInfo = myRepository.getChampionInfoByChampionKey(championKey)
            _championInfoLiveData.postValue(championInfo)
        }
    }

}