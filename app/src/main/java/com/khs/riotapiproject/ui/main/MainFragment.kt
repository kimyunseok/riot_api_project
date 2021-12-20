package com.khs.riotapiproject.ui.main

import androidx.fragment.app.viewModels
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.FragmentMainBinding
import com.khs.riotapiproject.ui.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.viewmodel.MainViewModel
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MainRepositoryViewModelFactory

class MainFragment: BaseFragmentForViewBinding<FragmentMainBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_main

    private val mainViewModel: MainViewModel by viewModels { MainRepositoryViewModelFactory(MainRepository()) }

    override fun init() {
        mainViewModel.getRankingData()
    }
}