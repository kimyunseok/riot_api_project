package com.khs.riotapiproject.view.main

import android.os.Bundle
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
import com.khs.riotapiproject.util.UserRankingHolderModelDiffUtil
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.view.info.ChampionInfoFragment
import com.khs.riotapiproject.view.info.ChampionListFragment
import com.khs.riotapiproject.view.info.UserInfoFragment
import com.khs.riotapiproject.viewmodel.aac.ChampionRotationViewModel
import com.khs.riotapiproject.viewmodel.aac.UserSoloRankTop10ViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel
import com.khs.riotapiproject.viewmodel.ui.MainNavigationHolderModel
import com.khs.riotapiproject.viewmodel.ui.UserRankingHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class MainFragment: BaseFragmentForViewBinding<FragmentMainBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_main

    private val userSoloRankTop10ViewModel: UserSoloRankTop10ViewModel by viewModels { MyRepositoryViewModelFactory(MyRepository()) }
    private val championRotationViewModel: ChampionRotationViewModel by viewModels { MyRepositoryViewModelFactory(MyRepository()) }

    lateinit var rankingRecyclerViewAdapter: SoloRankingRecyclerViewAdapter

    override fun init() {
        viewDataBinding.inputSummonerName = ""
        setUpBtnListener()
        setUpNavigationRecyclerView()

        setUpObserver()

        // Caching Data ????????????.
        getRankingDataAtLocalDB()
        getRotationList()

        // ???????????? ????????? ???????????? (?????? ??????????????? 2???.)
        setRotationList()
        getRankingData()
    }

    private fun setUpObserver() {
        //Rotation ????????? ????????? ?????????.
        championRotationViewModel.rotationChampionList.observe(viewLifecycleOwner) {
            //????????? ??????
            for(data in it) {
                context?.let { mContext ->
                    val type = "png"
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.championInfo.championId, type).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.championInfo.championId,
                                mContext.getString(R.string.champion_icon_url_front)
                                        + mContext.getString(R.string.lol_version)
                                        + mContext.getString(R.string.champion_icon_url_back),
                                type
                            )
                    }
                }
            }

            val championList = it.toMutableList()
            championList.sortWith(compareBy { data -> data.championInfo.championName })
            setUpRotationChampionRecyclerView(championList)
        }

        // ???????????? ????????? ?????? ??????????????? getRotationList() ??????.
        championRotationViewModel.setRotationChampionListCompleteLiveData.observe(viewLifecycleOwner) {
            getRotationList()
        }

        // ?????? ?????? ????????? ?????? ??? ????????????.
        userSoloRankTop10ViewModel.soloRankTop10AtLocalDBListLiveData.observe(viewLifecycleOwner) {
            setUpRankingRecyclerView(it)
        }

        userSoloRankTop10ViewModel.soloRankTop10ListFromServerLiveData.observe(viewLifecycleOwner) {
            // ????????? ?????? ????????? id??? ?????? ??????(icon, Level) ????????? ??? ?????????????????? ??????
            if(viewDataBinding.soloRankRecyclerView.adapter == null) {
                setUpRankingRecyclerView(it)
            } else {
                refreshRankingRecyclerView(it)
            }

            // OLD Data Remove.
            for(data in it) {
                userSoloRankTop10ViewModel.deleteUserInfoAtLocalDB(data.userRankingInfo.summonerID)
            }

            // ????????? ?????? ?????? ???????????? ????????? ???????????? ?????? ????????? Room DB??? ??????
            for(data in it) {
                // ?????? ????????? ??????
                context?.let { mContext ->
                    val type = "png"
                    if(ImageSaveUtil(mContext).checkAlreadySaved(data.getIconID().toString(), type).not()) {
                        ImageSaveUtil(mContext)
                            .imageToCache(
                                data.getIconID().toString(),
                                mContext.getString(R.string.profile_icon_url_front)
                                        + mContext.getString(R.string.lol_version)
                                        + mContext.getString(R.string.profile_icon_url_back),
                                type
                            )
                    }
                }

                // ?????? ?????? Room DB??? ??????(??????)
                userSoloRankTop10ViewModel.saveUserInfoAtLocalDB(data.userRankingInfo)
            }
        }
    }

    private fun getRankingDataAtLocalDB() {
        userSoloRankTop10ViewModel.getRankingDataFromLocalDB()
    }

    private fun getRankingData() {
        userSoloRankTop10ViewModel.getRankingDataFromServer()
    }

    private fun setRotationList() {
        championRotationViewModel.setRotationListFromServer(context?.getString(R.string.lol_version).toString())
    }

    private fun getRotationList() {
        championRotationViewModel.getRotationChampionData()
    }

    private fun setUpRankingRecyclerView(itemList: List<UserRankingHolderModel>) {
        rankingRecyclerViewAdapter = SoloRankingRecyclerViewAdapter(itemList, ::showUserInfoFragment)
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
            MainNavigationHolderModel("????????? ??????", ::showChampionListFragment)
        )
        viewDataBinding.navigationMenuRecyclerView.apply {
            adapter = MainNavigationRecyclerViewAdapter(context, menuList)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun refreshRankingRecyclerView(newItemList: List<UserRankingHolderModel>) {
        val oldItemList = rankingRecyclerViewAdapter.itemList

        val diffUtil = DiffUtil.calculateDiff(UserRankingHolderModelDiffUtil(oldItemList.toMutableList(), newItemList.toMutableList()), false)
        diffUtil.dispatchUpdatesTo(rankingRecyclerViewAdapter)
        rankingRecyclerViewAdapter.itemList = newItemList
    }

    private fun setUpBtnListener() {
        viewDataBinding.searchBtn.setOnClickListener {
            showUserInfoFragment(viewDataBinding.inputSummonerName.toString())
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

    private fun showUserInfoFragment(userName: String) {
        if(userName.trim().isNotEmpty()) {
            val bundle = Bundle().apply { putString("summonerName", userName) }

            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                    R.id.main_container,
                    UserInfoFragment().apply { arguments = bundle }
                )
                ?.addToBackStack(null)
                ?.commit()
        } else {
            Toast.makeText(context, context?.getString(R.string.input_searching_summoner_name), Toast.LENGTH_SHORT).show()
        }
    }

}