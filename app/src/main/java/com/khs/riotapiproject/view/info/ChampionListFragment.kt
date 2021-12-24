package com.khs.riotapiproject.view.info

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.ChampionIconRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentChampionListBinding
import com.khs.riotapiproject.util.ImageSaveUtil
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.viewmodel.aac.ChampionAllViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class ChampionListFragment: BaseFragmentForViewBinding<FragmentChampionListBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_champion_list

    private val championAllViewModel: ChampionAllViewModel by viewModels {
        MyRepositoryViewModelFactory(
            MyRepository()
        )
    }
    override fun init() {
        championAllViewModel.getAllChampionData(context?.getString(R.string.lol_version).toString())
        setUpObserver()
    }

    private fun setUpObserver() {
        championAllViewModel.allChampionListLiveData.observe(viewLifecycleOwner) {
            val championList = mutableListOf<ChampionIconHolderModel>()
            for(championInfo in it) {
                val championIconHolderModel = ChampionIconHolderModel(championInfo)
                championList.add(championIconHolderModel)

                context?.let { mContext ->
                    val type = "png"
                    if(ImageSaveUtil(mContext).checkAlreadySaved(championInfo.championId, type).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                championInfo.championId,
                                mContext.getString(R.string.champion_icon_url_front)
                                        + mContext.getString(R.string.lol_version)
                                        + mContext.getString(R.string.champion_icon_url_back),
                                type
                            )
                    }
                }
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