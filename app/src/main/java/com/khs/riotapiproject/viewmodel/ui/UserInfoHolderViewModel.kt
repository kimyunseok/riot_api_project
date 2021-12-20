package com.khs.riotapiproject.viewmodel.ui

import com.khs.riotapiproject.model.data.UserInfo

class UserInfoHolderViewModel(val userInfo: UserInfo) {
    fun levelFormat(): String {
        return "Lv. ${userInfo.level}"
    }
}