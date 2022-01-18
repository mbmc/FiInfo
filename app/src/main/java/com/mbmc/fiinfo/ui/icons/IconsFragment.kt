package com.mbmc.fiinfo.ui.icons

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
import com.mbmc.fiinfo.databinding.FragmentIconsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class IconsFragment : DialogFragment() {

    private lateinit var binding: FragmentIconsBinding
    private val viewModel: IconsViewModel by viewModels()
    private val adapter = IconAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentIconsBinding.inflate(layoutInflater)
        bindUi()
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.close) { _, _ -> dismiss() }
            .create()
    }

    private fun bindUi() {
        binding.icons.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.icons.collectLatest {
                    binding.progress.visibility = View.GONE
                    adapter.submitList(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "IconsFragment"
    }
}