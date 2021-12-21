package com.khs.riotapiproject.view.main

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.SoloRankingRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentMainBinding
import com.khs.riotapiproject.util.ImageSaveUtil
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.view.search.SearchActivity
import com.khs.riotapiproject.viewmodel.aac.MainViewModel
import com.khs.riotapiproject.viewmodel.repository.MainRepository
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MainRepositoryViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            if(rankingData.code == 200) {
                viewModel.getUserInfoListByIds()
            } else {
                //Can't Get Ranking List
                Toast.makeText(context, context?.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        }

        // Step 2. 불러온 랭킹 정보의 id로 유저 정보(icon, Level) 불러온 후 리사이클러뷰 셋팅
        viewModel.userInfoListLiveData.observe(viewLifecycleOwner) {
            val userInfoList = mutableListOf<UserInfoHolderModel>()
            for(data in it) {
                userInfoList.add(UserInfoHolderModel(data))
            }
            setUpRecyclerView(userInfoList)

            // Step 3. 불러온 랭킹 정보 이미지는 캐시에 저장하고 유저 정보는 Room DB에 저장
            CoroutineScope(Dispatchers.IO).launch {
                for(data in it) {
                    context?.let { mContext ->
                        if(ImageSaveUtil(mContext).checkAlreadySaved(data.iconID).not()) {
                            Log.d("GlideImageLoad", "${data.iconID} is Not Saved Image.")
                            ImageSaveUtil(mContext)
                                .imageToCache(
                                    data.iconID.toString(),
                                    "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon"
                                )
                        }
                    }
                }
            }
        }
    }

    private fun getRankingData() {
        viewModel.getRankingData()
    }

    private fun getRotationList() {
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