package com.mbmc.fiinfo.ui.stats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mbmc.fiinfo.data.Stat
import com.mbmc.fiinfo.databinding.ItemStatBinding

class StatAdapter : ListAdapter<Stat, StatAdapter.ViewHolder>(Comparator()) {

    inner class ViewHolder(private val binding: ItemStatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: Stat?) {
            with(binding) {
                stat?.let {
                    this.stat = stat
                    executePendingBindings()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemStatBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class Comparator : DiffUtil.ItemCallback<Stat>() {
    override fun areItemsTheSame(oldItem: Stat, newItem: Stat): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Stat, newItem: Stat): Boolean =
        oldItem.count == newItem.count
                && oldItem.type == newItem.type
                && oldItem.ssid == newItem.ssid
}