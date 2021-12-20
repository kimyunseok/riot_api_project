package com.khs.riotapiproject.view.main

import com.khs.riotapiproject.R
import com.khs.riotapiproject.common.GlobalApplication
import com.khs.riotapiproject.databinding.ActivityMainBinding
import com.khs.riotapiproject.view.base.BaseActivityForViewBinding

class MainActivity: BaseActivityForViewBinding<ActivityMainBinding>() {
    override val layoutID: Int
        get() = R.layout.activity_main

    override fun init() {
        setDevelopmentAPIKey()
        showMainFragment()
    }

    private fun showMainFragment() {
        supportFragmentManager.beginTransaction().replace(viewDataBinding.mainContainer.id, MainFragment()).commit()
    }

    private fun setDevelopmentAPIKey() {
        GlobalApplication.mySharedPreferences.setString("developmentAPIKey", applicationContext.getString(R.string.development_api_key))
    }

}