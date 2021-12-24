package com.khs.riotapiproject.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.ActivitySplashBinding
import com.khs.riotapiproject.view.base.BaseActivityForViewBinding
import com.khs.riotapiproject.view.main.MainActivity
import com.khs.riotapiproject.viewmodel.aac.ChampionAllViewModel
import com.khs.riotapiproject.viewmodel.aac.ChampionInfoViewModel
import com.khs.riotapiproject.viewmodel.aac.ChampionRotationViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity: BaseActivityForViewBinding<ActivitySplashBinding>() {
    override val layoutID: Int
        get() = R.layout.activity_splash

    private val championAllViewModel: ChampionAllViewModel by viewModels{ MyRepositoryViewModelFactory(MyRepository()) }
    private val championRotationViewModel: ChampionRotationViewModel by viewModels{ MyRepositoryViewModelFactory(MyRepository()) }

    override fun init() {
        setUpObserver()
        championAllViewModel.getAllChampionData(baseContext.getString(R.string.lol_version))
    }

    private fun setUpObserver() {
        championAllViewModel.allChampionListLiveData.observe(this) {
            championRotationViewModel.setRotationListFromServer(baseContext.getString(R.string.lol_version))
        }
        championRotationViewModel.setRotationChampionListCompleteLiveData.observe(this) {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed() - 데이터 불러오는 동안 뒤로가기 방지.
    }
}