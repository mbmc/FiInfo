package com.mbmc.fiinfo.ui.codes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.data.Code
import com.mbmc.fiinfo.databinding.FragmentCodeInstructionsBinding
import com.mbmc.fiinfo.helper.CodeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CodeInstructionsFragment(private val code: Code) : DialogFragment() {

    private lateinit var binding: FragmentCodeInstructionsBinding
    @Inject lateinit var codeManager: CodeManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCodeInstructionsBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.ok) { _, _ ->
                if (binding.hide.isChecked) {
                    codeManager.setHasSeenInstructions()
                }
                codeManager.openDialer(requireContext(), code.code)
            }
            .create()
    }

    companion object {
        const val TAG = "CodeInstructionsFragment"
    }
}