package com.khs.riotapiproject.view.main

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.RotationChampionRecyclerViewAdapter
import com.khs.riotapiproject.adapter.SoloRankingRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentMainBinding
import com.khs.riotapiproject.util.ImageSaveUtil
import com.khs.riotapiproject.util.UserInfoHolderModelDiffUtil
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.view.search.SearchActivity
import com.khs.riotapiproject.viewmodel.aac.ChampionInfoViewModel
import com.khs.riotapiproject.viewmodel.aac.UserInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.RotationChampionHolderModel
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class MainFragment: BaseFragmentForViewBinding<FragmentMainBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_main

    private val userInfoViewModel: UserInfoViewModel by viewModels { MyRepositoryViewModelFactory(MyRepository()) }
    private val championInfoViewModel: ChampionInfoViewModel by viewModels { MyRepositoryViewModelFactory(MyRepository()) }

    lateinit var rankingRecyclerViewAdapter: SoloRankingRecyclerViewAdapter

    override fun init() {
        setUpBtnListener()
        setUpObserver()
        getRankingDataAtLocalDB()

        getRotationList()

        //최소 데이터갱신 2분.
        getRankingData()
    }

    private fun setUpObserver() {
        // Step 1. 랭킹 정보 저장돼 있던 것 보여주기.
        userInfoViewModel.userInfoAtLocalDBListLiveData.observe(viewLifecycleOwner) {
            setUpRankingRecyclerView(it)
        }

        // Step 2. 랭킹 정보 서버에서 불러오기
        userInfoViewModel.rankingDataLiveData.observe(viewLifecycleOwner) {
                rankingData ->
            if(rankingData != null && rankingData.code == 200) {
                userInfoViewModel.getRankingUserInfoListByRankingDataFromServer()
            } else {
                //Can't Get Ranking List
                Toast.makeText(context, context?.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        }

        userInfoViewModel.userInfoListLiveData.observe(viewLifecycleOwner) {
            // Step 3. 불러온 랭킹 정보의 id로 유저 정보(icon, Level) 불러온 후 리사이클러뷰 셋팅
            if(userInfoViewModel.rankingRoomDBLoad.not()) {
                setUpRankingRecyclerView(it)
            } else {
                refreshRankingRecyclerView(it)
            }

            // Step 4. 불러온 랭킹 정보 이미지는 캐시에 저장하고 유저 정보는 Room DB에 저장
            userInfoViewModel.clearUserInfoAtLocalDB()

            for(data in it) {
                // 4 - 1. 유저 아이콘 캐싱
                context?.let { mContext ->
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.getIconID().toString()).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.getIconID().toString(),
                                mContext.getString(R.string.profile_icon_url)
                            )
                    }
                }

                // 4 - 2. 유저 정보 Room DB에 저장(캐싱)
                userInfoViewModel.saveUserInfoAtLocalDB(data.userInfo)
            }
        }

        //Rotation 챔피언 리스트 불러옴.
        championInfoViewModel.rotationChampionListLiveData.observe(viewLifecycleOwner) {

            //이미지 캐싱
            for(data in it) {
                context?.let { mContext ->
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.championInfo.championId).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.championInfo.championId,
                                mContext.getString(R.string.champion_icon_url)
                            )
                    }
                }
            }

            val championList = it.toMutableList()
            championList.sortWith(compareBy { data -> data.championInfo.championName })
            setUpRotationChampionRecyclerView(championList)
        }
    }

    private fun getRankingDataAtLocalDB() {
        userInfoViewModel.getRankingDataFromLocalDB()
    }

    private fun getRankingData() {
        userInfoViewModel.getRankingDataFromServer()
    }

    private fun getRotationList() {
        championInfoViewModel.getRotationList()
    }

    private fun setUpRankingRecyclerView(itemList: List<UserInfoHolderModel>) {
        rankingRecyclerViewAdapter = SoloRankingRecyclerViewAdapter(itemList)
        viewDataBinding.soloRankRecyclerView.apply {
            adapter = rankingRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpRotationChampionRecyclerView(itemList: List<RotationChampionHolderModel>) {
        viewDataBinding.rotationRecyclerView.apply {
            adapter = RotationChampionRecyclerViewAdapter(context, itemList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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