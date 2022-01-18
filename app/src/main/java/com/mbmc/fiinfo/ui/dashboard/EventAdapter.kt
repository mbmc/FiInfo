package com.mbmc.fiinfo.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mbmc.fiinfo.data.Event
import com.mbmc.fiinfo.databinding.ItemEventBinding

class EventAdapter : PagingDataAdapter<Event, EventAdapter.ViewHolder>(Comparator()) {

    inner class ViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event?) {
            with(binding) {
                event?.let {
                    this.event = event
                    executePendingBindings()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class Comparator : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
        oldItem.timestamp == newItem.timestamp
}
