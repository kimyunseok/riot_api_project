package com.khs.riotapiproject.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.ActivitySplashBinding
import com.khs.riotapiproject.view.base.BaseActivityForViewBinding
import com.khs.riotapiproject.view.main.MainActivity
import com.khs.riotapiproject.viewmodel.aac.ChampionInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity: BaseActivityForViewBinding<ActivitySplashBinding>() {
    override val layoutID: Int
        get() = R.layout.activity_splash

    private val championInfoViewModel: ChampionInfoViewModel by viewModels{ MyRepositoryViewModelFactory(MyRepository()) }

    override fun init() {
        setUpObserver()
        championInfoViewModel.getAllChampionDataFromServer()
    }

    private fun setUpObserver() {
        championInfoViewModel.allChampionListLiveData.observe(this) {
            championInfoViewModel.getRotationListFromServer()
        }
        championInfoViewModel.rotationChampionListLiveData.observe(this) {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed() - 데이터 불러오는 동안 뒤로가기 방지.
    }
}