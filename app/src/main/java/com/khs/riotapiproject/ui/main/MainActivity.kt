package com.khs.riotapiproject.ui.main

import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.ActivityMainBinding
import com.khs.riotapiproject.ui.base.BaseActivityForViewBinding

class MainActivity: BaseActivityForViewBinding<ActivityMainBinding>() {
    override val layoutID: Int
        get() = R.layout.activity_main

    override fun init() {
        showMainFragment()
    }

    private fun showMainFragment() {
        supportFragmentManager.beginTransaction().replace(viewDataBinding.mainContainer.id, MainFragment()).commit()
    }

}