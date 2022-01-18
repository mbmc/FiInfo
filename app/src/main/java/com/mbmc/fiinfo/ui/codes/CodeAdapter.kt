package com.mbmc.fiinfo.ui.codes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mbmc.fiinfo.data.Code
import com.mbmc.fiinfo.databinding.ItemCodeBinding

class CodeAdapter(
    private val clickListener: (code: Code) -> Unit
) : ListAdapter<Code, CodeAdapter.ViewHolder>(Comparator()) {

    inner class ViewHolder(val binding: ItemCodeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.code?.let {
                    clickListener.invoke(it)
                }
            }
        }

        fun bind(code: Code) {
            with(binding) {
                this.code = code
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class Comparator : DiffUtil.ItemCallback<Code>() {
    override fun areItemsTheSame(oldItem: Code, newItem: Code): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Code, newItem: Code): Boolean =
        oldItem.ordinal == newItem.ordinal
}