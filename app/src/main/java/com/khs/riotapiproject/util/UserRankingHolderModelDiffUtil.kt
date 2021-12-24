package com.khs.riotapiproject.util

import androidx.recyclerview.widget.DiffUtil
import com.khs.riotapiproject.viewmodel.ui.UserRankingHolderModel

class UserRankingHolderModelDiffUtil(val oldList: MutableList<UserRankingHolderModel>, val newList: MutableList<UserRankingHolderModel>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userRankingInfo.userInfoID == newList[newItemPosition].userRankingInfo.userInfoID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}