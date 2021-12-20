package com.khs.riotapiproject.view.main

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.SoloRankingRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentMainBinding
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.view.search.SearchActivity
import com.khs.riotapiproject.viewmodel.aac.MainViewModel
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MainRepositoryViewModelFactory

class MainFragment: BaseFragmentForViewBinding<FragmentMainBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_main

    private val viewModel: MainViewModel by viewModels { MainRepositoryViewModelFactory(MainRepository()) }

    override fun init() {
        setUpBtnListener()
        setUpObserver()
        getRankingData()
    }

    private fun setUpObserver() {
        // Step 1. 랭킹 정보 불러오기
        viewModel.rankingDataLiveData.observe(viewLifecycleOwner) {
                rankingData ->
            Log.d("Ranking Observe", "Ranking Data Hase Been Observed.")
            if(rankingData.code == 200) {
                viewModel.getUserInfoListByIds()
            } else {
                //exitProcess(0)
            }
        }

        // Step 2. 불러온 랭킹 정보의 id로 유저 정보(icon, Level) 불러온 후 리사이클러뷰 셋팅
        viewModel.userInfoListLiveData.observe(viewLifecycleOwner) {
            setUpRecyclerView(it)
        }
    }

    private fun getRankingData() {
        viewModel.getRankingData()
    }

    private fun setUpRecyclerView(itemList: List<UserInfoHolderModel>) {
        viewDataBinding.soloRankRecyclerView.apply {
            adapter = SoloRankingRecyclerViewAdapter(itemList)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpBtnListener() {
        viewDataBinding.searchTextView.setOnClickListener {
            requireActivity().startActivity(Intent(context, SearchActivity::class.java))
        }
    }

}