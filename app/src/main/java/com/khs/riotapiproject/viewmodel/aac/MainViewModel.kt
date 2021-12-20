package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khs.riotapiproject.model.data.RankingData
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val _rankingDataLiveData = MutableLiveData<RankingData>()
    val rankingDataLiveData: LiveData<RankingData>
        get() = _rankingDataLiveData

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
}