package com.mbmc.fiinfo.ui.stats

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : DialogFragment() {

    private lateinit var binding: FragmentStatsBinding

    private val viewModel: StatsViewModel by viewModels()
    private val adapter = StatAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentStatsBinding.inflate(layoutInflater)
        bindUi()
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.close) { _, _ -> dismiss() }
            .create()
    }

    private fun bindUi() {
        binding.stats.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stats.collectLatest {
                    binding.progress.visibility = View.GONE
                    adapter.submitList(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "StatsFragment"
    }
}