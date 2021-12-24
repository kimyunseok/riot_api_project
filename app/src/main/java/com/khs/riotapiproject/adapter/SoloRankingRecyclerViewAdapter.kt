package com.khs.riotapiproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.databinding.HolderSoloRankUserInfoBinding
import com.khs.riotapiproject.viewmodel.ui.UserRankingHolderModel

class SoloRankingRecyclerViewAdapter(var itemList: List<UserRankingHolderModel>, val onClick: (String) -> Unit): RecyclerView.Adapter<SoloRankingRecyclerViewAdapter.SoloRankingRecyclerViewHolder>() {

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
        fun bind(item: UserRankingHolderModel) {
            viewDataBinding.model = item

            itemView.setOnClickListener {
                onClick(item.userRankingInfo.name)
            }
        }
    }
}