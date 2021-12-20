package com.khs.riotapiproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.model.retrofit.data.RankingData
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val _rankingDataLiveData = MutableLiveData<RankingData>()
    val rankingDataLiveData: LiveData<RankingData>
        get() = _rankingDataLiveData

    fun getRankingData() {
        CoroutineScope(Dispatchers.IO).launch {
            mainRepository.getRanking().let {
                response ->

                response.body()?.let {
                    it.code = response.code()
                    it.message = response.message()
                }

                if(response.isSuccessful) {
                    Log.d("MainViewModel", "Get Ranking API Is Success. code : ${response.code()}, message : ${response.message()}")
                } else {
                    Log.d("MainViewModel", "Get Ranking API Is Failed. code : ${response.code()}, message : ${response.message()}")
                }
                _rankingDataLiveData.postValue(response.body())
            }
        }
    }
}