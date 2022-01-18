package com.mbmc.fiinfo.ui.icons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mbmc.fiinfo.data.Icon
import com.mbmc.fiinfo.databinding.ItemIconBinding

class IconAdapter : ListAdapter<Icon, IconAdapter.ViewHolder>(Comparator()) {

    inner class ViewHolder(private val binding: ItemIconBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: Icon) {
            with(binding) {
                this.icon = icon
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class Comparator : DiffUtil.ItemCallback<Icon>() {
    override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Icon, newItem: Icon): Boolean =
        oldItem.descriptionRes == newItem.descriptionRes
                && oldItem.drawableRes == newItem.drawableRes
}