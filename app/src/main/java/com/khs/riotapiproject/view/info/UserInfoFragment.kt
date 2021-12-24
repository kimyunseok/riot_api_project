package com.khs.riotapiproject.view.info

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.FragmentUserInfoBinding
import com.khs.riotapiproject.view.base.BaseFragmentForViewBinding
import com.khs.riotapiproject.viewmodel.aac.UserInfoViewModel
import com.khs.riotapiproject.viewmodel.repository.MyRepository
import com.khs.riotapiproject.viewmodel.viewmodelfactory.MyRepositoryViewModelFactory

class UserInfoFragment: BaseFragmentForViewBinding<FragmentUserInfoBinding>() {
    override val layoutID: Int
        get() = R.layout.fragment_user_info

    private val userInfoViewModel: UserInfoViewModel by viewModels {
        MyRepositoryViewModelFactory(MyRepository())
    }

    override fun init() {
        viewDataBinding.viewModel = userInfoViewModel

        setUpObserver()
        getUserInfoByName()
    }

    private fun setUpObserver() {
        userInfoViewModel.errorCodeLiveData.observe(viewLifecycleOwner) {
            if(it == 404) {
                Toast.makeText(context, context?.getString(R.string.no_found), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context?.getString(R.string.error_occur), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserInfoByName() {
        val summonerName = arguments?.getString("summonerName", "NO_NAME").toString()
        // 최초에는 로컬 DB
        userInfoViewModel.getUserInfoAtLocalDB(summonerName)

        // 서버에서 데이터 한번 더 요청. (최소 대기시간 2분)
        userInfoViewModel.getUserInfoBySummonerName(summonerName)
    }
}