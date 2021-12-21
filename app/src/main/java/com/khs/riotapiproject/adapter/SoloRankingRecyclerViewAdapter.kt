package com.khs.riotapiproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.databinding.HolderSoloRankUserInfoBinding
import com.khs.riotapiproject.viewmodel.ui.UserInfoHolderModel

class SoloRankingRecyclerViewAdapter(var itemList: List<UserInfoHolderModel>): RecyclerView.Adapter<SoloRankingRecyclerViewAdapter.SoloRankingRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SoloRankingRecyclerViewHolder {
        val viewDataBinding = HolderSoloRankUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoloRankingRecyclerViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: SoloRankingRecyclerViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class SoloRankingRecyclerViewHolder(private val viewDataBinding: HolderSoloRankUserInfoBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(item: UserInfoHolderModel) {
            viewDataBinding.model = item
        }
    }
}