package com.khs.riotapiproject.view.info

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.khs.riotapiproject.R
import com.khs.riotapiproject.adapter.ChampionMasteryRecyclerViewAdapter
import com.khs.riotapiproject.databinding.FragmentUserInfoBinding
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.viewmodel.aac.UserChampionMasteryViewModel
import com.khs.riotapiproject.viewmodel.aac.UserInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.ui.ChampionMasteryHolderModel
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class UserInfoFragment: BaseFragmentForViewBinding<FragmentUserInfoBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_user_info

    private val userInfoViewModel: UserInfoViewModel by viewModels {
        MyRepositoryViewModelFactory(MyRepository())
    }

    private val userChampionMasteryViewModel: UserChampionMasteryViewModel by viewModels {
        MyRepositoryViewModelFactory(MyRepository())
    }

    private val summonerName: String by lazy {
        arguments?.getString("summonerName", "NO_NAME").toString()
    }

    override fun init() {
        viewDataBinding.viewModel = userInfoViewModel

        setUpObserver()
        getUserInfoByName()
        getUserChampionMastery()
    }

    private fun setUpObserver() {
        userInfoViewModel.errorCodeLiveData.observe(viewLifecycleOwner) {
            if(it == 404) {
                Toast.makeText(context, context?.getString(R.string.no_found), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context?.getString(R.string.error_occur), Toast.LENGTH_SHORT).show()
            }
        }

        userChampionMasteryViewModel.mostChampionListLiveData.observe(viewLifecycleOwner) {
            setUpRecyclerView(it)
        }
    }

    private fun getUserInfoByName() {
        // 최초에는 로컬 DB
        userInfoViewModel.getUserInfoAtLocalDB(summonerName)

        // 서버에서 데이터 한번 더 요청. (최소 대기시간 2분)
        userInfoViewModel.getUserInfoBySummonerName(summonerName)
    }

    private fun getUserChampionMastery() {
        // 최초에는 로컬 DB
        userChampionMasteryViewModel.getChampionMasteryFromLocalDBSummonerName(summonerName)

        // 서버에서 데이터 한번 더 요청. (최소 대기시간 2분)
        userChampionMasteryViewModel.getChampionMasteryFromServerBySummonerName(summonerName)
    }

    private fun setUpRecyclerView(itemList: List<ChampionMasteryHolderModel>) {
        // 최초에는 로컬 DB
        viewDataBinding.summonerChampionMasteryRecyclerView.apply {
            adapter = ChampionMasteryRecyclerViewAdapter(activity, itemList)
            layoutManager = LinearLayoutManager(context)
        }
    }
}