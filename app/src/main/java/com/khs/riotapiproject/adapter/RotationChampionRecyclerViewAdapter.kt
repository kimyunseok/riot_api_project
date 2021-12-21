package com.khs.riotapiproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.databinding.HolderRotationChampionBinding
import com.khs.riotapiproject.viewmodel.ui.RotationChampionHolderModel

class RotationChampionRecyclerViewAdapter(var itemList: List<RotationChampionHolderModel>): RecyclerView.Adapter<RotationChampionRecyclerViewAdapter.RotationChampionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RotationChampionViewHolder {
        val viewDataBinding = HolderRotationChampionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RotationChampionViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RotationChampionViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class RotationChampionViewHolder(private val viewDataBinding: HolderRotationChampionBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {
            fun bind(item: RotationChampionHolderModel) {
                viewDataBinding.model = item
            }
        }

}