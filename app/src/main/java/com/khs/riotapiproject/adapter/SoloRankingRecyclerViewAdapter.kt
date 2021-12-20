package com.khs.riotapiproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.databinding.HolderUserInfoBinding
import com.khs.riotapiproject.model.data.UserInfo
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderViewModel

class SoloRankingRecyclerViewAdapter(private val itemList: List<UserInfo>): RecyclerView.Adapter<SoloRankingRecyclerViewAdapter.SoloRankingRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SoloRankingRecyclerViewHolder {
        val viewDataBinding = HolderUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoloRankingRecyclerViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: SoloRankingRecyclerViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class SoloRankingRecyclerViewHolder(private val viewDataBinding: HolderUserInfoBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(item: UserInfo) {
            viewDataBinding.model = UserInfoHolderViewModel(item)
        }
    }
}