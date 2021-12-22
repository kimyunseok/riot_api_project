package com.khs.riotapiproject.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.HolderRotationChampionBinding
import com.khs.riotapiproject.view.info.ChampionInfoFragment
import com.khs.riotapiproject.view.main.MainActivity
import com.khs.riotapiproject.viewmodel.ui.RotationChampionHolderModel

class RotationChampionRecyclerViewAdapter(val context: Context, var itemList: List<RotationChampionHolderModel>): RecyclerView.Adapter<RotationChampionRecyclerViewAdapter.RotationChampionViewHolder>() {

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

                itemView.setOnClickListener {
                    val bundle = Bundle().apply { putString("championKey", item.championInfo.championKey) }

                    (context as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, ChampionInfoFragment().apply { arguments = bundle })
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

}