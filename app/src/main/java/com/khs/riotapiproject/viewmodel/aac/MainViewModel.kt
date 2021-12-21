package com.khs.riotapiproject.viewmodel.aac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.model.retrofit.data.ChampionData
import com.khs.riotapiproject.model.retrofit.data.RankingData
import com.khs.riotapiproject.model.room.data.ChampionInfo
import com.khs.riotapiproject.model.room.data.UserInfo
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import com.khs.riotapiproject.viewmodel.ui.RotationChampionHolderModel
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.ConnectException

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    private val _rankingDataLiveData = MutableLiveData<RankingData>()
    val rankingDataLiveData: LiveData<RankingData>
        get() = _rankingDataLiveData
    private val rankingDataDetailListValue: MutableList<RankingData.RankingDataDetail>
        get() = rankingDataLiveData.value?.entries?: mutableListOf()

    private val _userInfoAtLocalDBListLiveData = MutableLiveData<List<UserInfoHolderModel>>()
    val userInfoAtLocalDBListLiveData: LiveData<List<UserInfoHolderModel>>
        get() = _userInfoAtLocalDBListLiveData

    private val _userInfoListLiveData = MutableLiveData<List<UserInfoHolderModel>>()
    val userInfoListLiveData: LiveData<List<UserInfoHolderModel>>
        get() = _userInfoListLiveData

    private val _allChampionListLiveData = MutableLiveData<List<ChampionInfo>>()
    val allChampionListLiveData: LiveData<List<ChampionInfo>>
        get() = _allChampionListLiveData
    val allChampionListValue: List<ChampionInfo>
        get() = allChampionListLiveData.value?: mutableListOf()

    private val _rotationChampionListLiveData = MutableLiveData<List<RotationChampionHolderModel>>()
    val rotationChampionListLiveData: LiveData<List<RotationChampionHolderModel>>
        get() = _rotationChampionListLiveData

    var roomDBLoad = false

    // 2분에 한 번씩 데이터 가져오기 가능.
    val checkMinTimeForGetRankingData: Boolean by lazy {
        System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRankingDataTime", 0) > 120000
    }
    val checkMinTimeForGetRotationChampionData: Boolean by lazy {
        System.currentTimeMillis() - GlobalApplication.mySharedPreferences.getLong("getRotationChampionData", 0) > 120000
    }

    fun getRankingData() {
        if(checkMinTimeForGetRankingData) {
            GlobalApplication.mySharedPreferences.setLong("getRankingDataTime", System.currentTimeMillis())

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

                        Log.d("MainViewModel", "Get User Info API. code : ${response.code()}, message : ${response.message()}")

                        response.body()?.let {
                            it.code = response.code()
                            it.message = response.message()

                            val userInfo = UserInfo(
                                0,
                                it.profileIconId,
                                it.name,
                                "챌린저 ${rankingDataDetailListValue[idx].leaguePoints}점",
                                it.summonerLevel,
                                idx + 1,
                                rankingDataDetailListValue[idx].wins,
                                rankingDataDetailListValue[idx].losses
                            )

                            rankList.add(UserInfoHolderModel(userInfo))
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

    fun getRankingDataAtLocalDB() {
        mainRepository.getAllUserInfo().let {
            val rankList = mutableListOf<UserInfoHolderModel>()
            for(userInfo in it) {
                rankList.add(UserInfoHolderModel(userInfo))
            }
            if(rankList.isNotEmpty()) {
                roomDBLoad = true
            }
            rankList.sortWith(compareBy { userModel -> userModel.userInfo.rank })

            _userInfoAtLocalDBListLiveData.postValue(rankList)
        }
    }

    fun clearUserInfoAtLocalDB() {
        mainRepository.clearUserInfo()
    }

    fun saveUserInfoAtLocalDB(userInfo: UserInfo) {
        mainRepository.insertUserInfo(userInfo)
    }

    fun getRotationList() {
        if(checkMinTimeForGetRotationChampionData) {
            mainRepository.setAllChampionNoRotation()
            GlobalApplication.mySharedPreferences.setLong("getRotationChampionData", System.currentTimeMillis())

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mainRepository.getRotationChampionList().let {
                            response ->

                        Log.d("MainViewModel", "Get Rotation Champion API. code : ${response.code()}, message : ${response.message()}")

                        response.body()?.let {
                            it.code = response.code()
                            it.message = response.message()

                            for(rotationChampionId in it.freeChampionIds) {
                                mainRepository.setChampionRotation(rotationChampionId.toString())
                            }
                        }
                        val rotationListFromDB = mainRepository.getAllRotationChampionList()
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
                val rotationListFromDB = mainRepository.getAllRotationChampionList()
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
            mainRepository.clearChampionInfo()
            CoroutineScope(Dispatchers.IO).launch {
                GlobalApplication.mySharedPreferences.setString("lolVersion", version)
                mainRepository.getAllChampionData().let {
                        response ->
                    Log.d("MainViewModel", "Get All Champion DATA API. code : ${response.code()}, message : ${response.message()}")

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
                            mainRepository.insertChampionInfo(championInfo)
                        }

                        _allChampionListLiveData.postValue(championListForPostValue)
                    }
                }
            }
        } else {
            val championList = mainRepository.getAllChampionInfo()
            _allChampionListLiveData.postValue(championList)
        }

    }
}