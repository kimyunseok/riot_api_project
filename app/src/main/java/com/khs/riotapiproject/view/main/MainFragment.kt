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
        viewModel.rankingDataLiveData.observe(viewLifecycleOwner) {
                rankingData ->
            Log.d("Ranking Observe", "Ranking Data Hase Been Observed.")
            if(rankingData.code == 200) {
                val itemList = mutableListOf<UserInfoHolderModel>()

                val rankingLength = if(rankingData.entries.size > 10) {
                    10
                } else {
                    rankingData.entries.size
                }

                for(idx in 0 until rankingLength) {
                    val userInfo = UserInfoHolderModel(
                        context?.getString(R.string.icon_url).toString() + "1" + ".png",
                        rankingData.entries[idx].summonerName,
                        "챌린저 " + rankingData.entries[idx].leaguePoints + "점",
                        123,
                        idx + 1,
                        rankingData.entries[idx].wins,
                        rankingData.entries[idx].losses
                    )
                    itemList.add(userInfo)
                }

                setUpRecyclerView(itemList)
            } else {
                //exitProcess(0)
            }
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