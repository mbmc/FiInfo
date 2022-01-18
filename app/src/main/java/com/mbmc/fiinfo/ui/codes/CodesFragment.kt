package com.mbmc.fiinfo.ui.codes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.data.Code
import com.mbmc.fiinfo.databinding.FragmentCodesBinding
import com.mbmc.fiinfo.helper.CodeManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CodesFragment(private val all: Boolean = false) : DialogFragment() {

    private lateinit var binding: FragmentCodesBinding
    private lateinit var adapter: CodeAdapter
    @Inject lateinit var codeManager: CodeManager
    private val viewModel: CodesViewModel by lazy {
        CodesViewModel(all)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCodesBinding.inflate(layoutInflater)
        bindUi()
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.close) { _, _ -> dismiss() }
            .create()
    }

    private fun bindUi() {
        adapter = CodeAdapter { code ->
            dismiss()
            if (!codeManager.execute(code.code)) {
                showInstructions(code)
            }
        }

        binding.codes.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.codes.collectLatest {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun showInstructions(code: Code) {
        CodeInstructionsFragment(code).show(parentFragmentManager, CodeInstructionsFragment.TAG)
    }

    companion object {
        const val TAG = "CodesFragment"
    }
}