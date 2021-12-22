package com.khs.riotapiproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.databinding.HolderNavigationMenuBinding
import com.khs.riotapiproject.viewmodel.ui.MainNavigationHolderModel

class MainNavigationRecyclerViewAdapter(val context: Context, private val menuList: List<MainNavigationHolderModel>) :
    RecyclerView.Adapter<MainNavigationRecyclerViewAdapter.MainNavigationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainNavigationViewHolder {
        val viewDataBinding = HolderNavigationMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainNavigationViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: MainNavigationViewHolder, position: Int) {
        val item = menuList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class MainNavigationViewHolder(val viewDataBinding: HolderNavigationMenuBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {
            fun bind(item: MainNavigationHolderModel) {
                viewDataBinding.model = item

                itemView.setOnClickListener { item.onClick() }
            }
        }
}