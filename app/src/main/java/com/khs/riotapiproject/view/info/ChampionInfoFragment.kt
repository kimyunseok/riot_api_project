package com.khs.riotapiproject.view.info

import androidx.fragment.app.viewModels
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.FragmentChampionInfoBinding
import com.khs.riotapiproject.util.ImageSaveUtil
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.viewmodel.aac.ChampionInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class ChampionInfoFragment: BaseFragmentForViewBinding<FragmentChampionInfoBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_champion_info

    private val championInfoViewModel: ChampionInfoViewModel by viewModels{ MyRepositoryViewModelFactory(MyRepository()) }

    override fun init() {
        viewDataBinding.viewModel = championInfoViewModel
        setUpObserver()
        getChampionInfo()
    }

    private fun getChampionInfo() {
        val championKey = arguments?.getString("championKey", "NO_KEY").toString()
        championInfoViewModel.getChampionInfo(championKey)
    }

    private fun setUpObserver() {
        championInfoViewModel.championInfoLiveData.observe(viewLifecycleOwner) {
        val type = "jpg"
            context?.let { mContext ->
                if(ImageSaveUtil(mContext).checkAlreadySaved(it.championId, type).not()) {
                    ImageSaveUtil(mContext)
                        .imageToCache(
                            it.championId,
                            mContext.getString(R.string.champion_loading_image_url),
                            type,
                            "_0"
                        )
                }
            }
        }
    }
}