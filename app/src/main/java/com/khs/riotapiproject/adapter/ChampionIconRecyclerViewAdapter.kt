package com.khs.riotapiproject.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.khs.riotapiproject.R
import com.khs.riotapiproject.databinding.HolderChampionIconBinding
import com.khs.riotapiproject.view.info.ChampionInfoFragment
import com.khs.riotapiproject.viewmodel.ui.ChampionIconHolderModel

class ChampionIconRecyclerViewAdapter(private val activity: FragmentActivity?, var itemList: List<ChampionIconHolderModel>)
    : RecyclerView.Adapter<ChampionIconRecyclerViewAdapter.RotationChampionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RotationChampionViewHolder {
        val viewDataBinding = HolderChampionIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RotationChampionViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: RotationChampionViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class RotationChampionViewHolder(private val viewDataBinding: HolderChampionIconBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(item: ChampionIconHolderModel) {
            viewDataBinding.model = item

            itemView.setOnClickListener { showChampionInfoFragment(item.championInfo.championKey) }
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