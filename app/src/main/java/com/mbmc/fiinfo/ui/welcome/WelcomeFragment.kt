package com.mbmc.fiinfo.ui.welcome

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.databinding.FragmentWelcomeBinding
import com.mbmc.fiinfo.helper.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : DialogFragment() {

    private lateinit var binding: FragmentWelcomeBinding
    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.whats_new)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (binding.hide.isChecked) {
                    preferenceManager.setHasSeenWelcome()
                }
            }
            .create()
    }

    companion object {
        const val TAG = "WelcomeFragment"
    }
}