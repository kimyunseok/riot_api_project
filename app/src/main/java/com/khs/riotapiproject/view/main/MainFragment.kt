package com.khs.riotapiproject.view.main

import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.ChampionIconRecyclerViewAdapter
import com.khs.riotapiproject.adapter.MainNavigationRecyclerViewAdapter
import com.khs.riotapiproject.adapter.SoloRankingRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentMainBinding
import com.khs.riotapiproject.util.ImageSaveUtil
import com.khs.riotapiproject.util.UserInfoHolderModelDiffUtil
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.view.info.ChampionListFragment
import com.khs.riotapiproject.viewmodel.aac.ChampionInfoViewModel
import com.khs.riotapiproject.viewmodel.aac.UserInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel
import com.khs.riotapiproject.viewmodel.ui.MainNavigationHolderModel
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
        setUpNavigationRecyclerView()

        setUpObserver()

        // Caching Data 가져오기.
        getRankingDataAtLocalDB()
        getRotationList()

        // 서버에서 데이터 가져오기 (최소 데이터갱신 2분.)
        setRotationList()
        getRankingData()
    }

    private fun setUpObserver() {
        //Rotation 챔피언 리스트 불러옴.
        championInfoViewModel.rotationChampionList.observe(viewLifecycleOwner) {
            //이미지 캐싱
            for(data in it) {
                context?.let { mContext ->
                    val type = "png"
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.championInfo.championId, type).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.championInfo.championId,
                                mContext.getString(R.string.champion_icon_url),
                                type,
                                ""
                            )
                    }
                }
            }

            val championList = it.toMutableList()
            championList.sortWith(compareBy { data -> data.championInfo.championName })
            setUpRotationChampionRecyclerView(championList)
        }

        // 랭킹 정보 저장돼 있던 것 보여주기.
        userInfoViewModel.userInfoAtLocalDBListLiveData.observe(viewLifecycleOwner) {
            setUpRankingRecyclerView(it)
        }

        // 랭킹 정보 서버에서 불러온 데이터
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
            // 불러온 랭킹 정보의 id로 유저 정보(icon, Level) 불러온 후 리사이클러뷰 셋팅
            if(userInfoViewModel.rankingRoomDBLoad.not()) {
                setUpRankingRecyclerView(it)
            } else {
                refreshRankingRecyclerView(it)
            }

            // 불러온 랭킹 정보 이미지는 캐시에 저장하고 유저 정보는 Room DB에 저장
            userInfoViewModel.clearUserInfoAtLocalDB()

            for(data in it) {
                // 유저 아이콘 캐싱
                context?.let { mContext ->
                    val type = "png"
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.getIconID().toString(), type).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.getIconID().toString(),
                                mContext.getString(R.string.profile_icon_url),
                                type,
                                ""
                            )
                    }
                }

                // 유저 정보 Room DB에 저장(캐싱)
                userInfoViewModel.saveUserInfoAtLocalDB(data.userInfo)
            }
        }

        // 로테이션 리스트 새로 불러왔다면 getRotationList() 호출.
        championInfoViewModel.setRotationChampionListCompleteLiveData.observe(viewLifecycleOwner) {
            getRotationList()
        }
    }

    private fun getRankingDataAtLocalDB() {
        userInfoViewModel.getRankingDataFromLocalDB()
    }

    private fun getRankingData() {
        userInfoViewModel.getRankingDataFromServer()
    }

    private fun setRotationList() {
        championInfoViewModel.setRotationListFromServer(context?.getString(R.string.lol_version).toString())
    }

    private fun getRotationList() {
        championInfoViewModel.getRotationChampionData()
    }

    private fun setUpRankingRecyclerView(itemList: List<UserInfoHolderModel>) {
        rankingRecyclerViewAdapter = SoloRankingRecyclerViewAdapter(itemList)
        viewDataBinding.soloRankRecyclerView.apply {
            adapter = rankingRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpRotationChampionRecyclerView(itemList: List<ChampionIconHolderModel>) {
        viewDataBinding.rotationRecyclerView.apply {
            adapter = ChampionIconRecyclerViewAdapter(activity, itemList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpNavigationRecyclerView() {
        val menuList = mutableListOf(
            MainNavigationHolderModel("챔피언 목록", ::showChampionListFragment),
            MainNavigationHolderModel("준비 중", ::showReadyToastMessage),
            MainNavigationHolderModel("준비 중", ::showReadyToastMessage),
            MainNavigationHolderModel("준비 중", ::showReadyToastMessage),
            MainNavigationHolderModel("준비 중", ::showReadyToastMessage),
            MainNavigationHolderModel("준비 중", ::showReadyToastMessage),
            MainNavigationHolderModel("준비 중", ::showReadyToastMessage)
        )
        viewDataBinding.navigationMenuRecyclerView.apply {
            adapter = MainNavigationRecyclerViewAdapter(context, menuList)
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
        viewDataBinding.searchBtn.setOnClickListener {
            //TODO : 유저 검색 기능
        }

        viewDataBinding.mainMenuBtn.setOnClickListener {
            viewDataBinding.mainDrawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showChampionListFragment() {
        viewDataBinding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, ChampionListFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun showReadyToastMessage() {
        viewDataBinding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        Toast.makeText(context, context?.getString(R.string.ready_job), Toast.LENGTH_SHORT).show()
    }



}