package com.khs.riotapiproject.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.HolderChampionMasteryBinding
import com.khs.riotapiproject.view.info.ChampionInfoFragment
import com.khs.riotapiproject.viewmodel.ui.ChampionMasteryHolderModel

class ChampionMasteryRecyclerViewAdapter(private val activity: FragmentActivity?, private val itemList: List<ChampionMasteryHolderModel>) :
    RecyclerView.Adapter<ChampionMasteryRecyclerViewAdapter.ChampionMasteryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionMasteryViewHolder {
        val viewDataBinding = HolderChampionMasteryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChampionMasteryViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: ChampionMasteryViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ChampionMasteryViewHolder(val viewDataBinding: HolderChampionMasteryBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(item: ChampionMasteryHolderModel) {
            viewDataBinding.model = item

            itemView.setOnClickListener { showChampionInfoFragment(item.championMastery.championKey) }
        }
    }

    private fun showChampionInfoFragment(championKey: String) {
        val bundle = Bundle().apply { putString("championKey", championKey) }

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_container, ChampionInfoFragment().apply { arguments = bundle })
            ?.addToBackStack(null)
            ?.commit()
    }
}