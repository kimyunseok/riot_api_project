package com.khs.riotapiproject.view.info

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.ChampionIconRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentChampionListBinding
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.viewmodel.aac.ChampionInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class ChampionListFragment: BaseFragmentForViewBinding<FragmentChampionListBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_champion_list

    private val championInfoViewModel: ChampionInfoViewModel by viewModels {
        MyRepositoryViewModelFactory(
            MyRepository()
        )
    }
    override fun init() {
        championInfoViewModel.getAllChampionData()
        setUpObserver()
    }

    private fun setUpObserver() {
        championInfoViewModel.allChampionListLiveData.observe(viewLifecycleOwner) {
            val championList = mutableListOf<ChampionIconHolderModel>()
            for(championInfo in it) {
                val championIconHolderModel = ChampionIconHolderModel(championInfo)
                championList.add(championIconHolderModel)
            }
            championList.sortWith(compareBy { champion -> champion.championInfo.championName })
            setUpChampionListRecyclerView(championList)
        }
    }

    private fun setUpChampionListRecyclerView(itemList: List<ChampionIconHolderModel>) {
        viewDataBinding.championListRecyclerView.apply {
            adapter = ChampionIconRecyclerViewAdapter(activity, itemList)
            layoutManager = GridLayoutManager(context, 4)
        }
    }
}