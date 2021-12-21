package com.khs.riotapiproject.view.main

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.SoloRankingRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentMainBinding
import com.khs.riotapiproject.util.ImageSaveUtil
import com.khs.riotapiproject.util.UserInfoHolderModelDiffUtil
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

    lateinit var rankingRecyclerViewAdapter: SoloRankingRecyclerViewAdapter

    override fun init() {
        setUpBtnListener()
        setUpObserver()
        getRankingDataAtLocalDB()

        //최소 데이터갱신 1분.
        if(viewModel.checkMinTimeForGetData) {
            getRankingData()
            getRotationList()
        }
    }

    private fun setUpObserver() {
        // Step 1. 랭킹 정보 저장돼 있던 것 보여주기.
        viewModel.userInfoAtLocalDBListLiveData.observe(viewLifecycleOwner) {
            setUpRankingRecyclerView(it)
        }

        // Step 2. 랭킹 정보 서버에서 불러오기
        viewModel.rankingDataLiveData.observe(viewLifecycleOwner) {
                rankingData ->
            if(rankingData.code == 200) {
                viewModel.getUserInfoListByIds()
            } else {
                //Can't Get Ranking List
                Toast.makeText(context, context?.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.userInfoListLiveData.observe(viewLifecycleOwner) {
            // Step 3. 불러온 랭킹 정보의 id로 유저 정보(icon, Level) 불러온 후 리사이클러뷰 셋팅
            if(viewModel.roomDBLoad.not()) {
                setUpRankingRecyclerView(it)
            } else {
                refreshRankingRecyclerView(it)
            }

            // Step 4. 불러온 랭킹 정보 이미지는 캐시에 저장하고 유저 정보는 Room DB에 저장
            viewModel.clearUserInfoAtLocalDB()

            for(data in it) {
                // 4 - 1. 유저 아이콘 캐싱
                context?.let { mContext ->
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.getIconID()).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.getIconID().toString(),
                                mContext.getString(R.string.profile_icon_url)
                            )
                    }
                }

                // 4 - 2. 유저 정보 Room DB에 저장(캐싱)
                viewModel.saveUserInfoAtLocalDB(data.userInfo)
            }
        }
    }

    private fun getRankingDataAtLocalDB() {
        viewModel.getRankingDataAtLocalDB()
    }

    private fun getRankingData() {
        viewModel.getRankingData()
    }

    private fun getRotationList() {

    }

    private fun setUpRankingRecyclerView(itemList: List<UserInfoHolderModel>) {
        rankingRecyclerViewAdapter = SoloRankingRecyclerViewAdapter(itemList)
        viewDataBinding.soloRankRecyclerView.apply {
            adapter = rankingRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun refreshRankingRecyclerView(newItemList: List<UserInfoHolderModel>) {
        val oldItemList = rankingRecyclerViewAdapter.itemList

        val diffUtil = DiffUtil.calculateDiff(UserInfoHolderModelDiffUtil(oldItemList.toMutableList(), newItemList.toMutableList()), false)
        diffUtil.dispatchUpdatesTo(rankingRecyclerViewAdapter)
        rankingRecyclerViewAdapter.itemList = newItemList
    }

    private fun setUpBtnListener() {
        viewDataBinding.searchTextView.setOnClickListener {
            requireActivity().startActivity(Intent(context, SearchActivity::class.java))
        }
    }

}