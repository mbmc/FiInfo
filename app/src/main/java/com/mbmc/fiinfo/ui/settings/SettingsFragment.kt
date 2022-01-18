package com.mbmc.fiinfo.ui.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mbmc.fiinfo.BuildConfig
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.databinding.FragmentSettingsBinding
import com.mbmc.fiinfo.helper.BackupManager
import com.mbmc.fiinfo.service.EventService
import com.mbmc.fiinfo.ui.codes.CodesFragment
import com.mbmc.fiinfo.ui.icons.IconsFragment
import com.mbmc.fiinfo.ui.stats.StatsFragment
import com.mbmc.fiinfo.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var permissionsChecker: ActivityResultLauncher<Array<String>>
    private lateinit var save: ActivityResultLauncher<Unit>
    private lateinit var restore: ActivityResultLauncher<Unit>

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionsChecker = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            setWifiSsid()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect {
                    when (it) {
                        is Result.Success, is Result.Error -> {
                            Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Result.IsLoading -> {
                            if (it.state) {
                                binding.progress.visibility = View.VISIBLE
                            } else {
                                binding.progress.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }

        save = registerForActivityResult(BackupManager.Save()) {
            it?.let { uri ->
                viewModel.save(uri)
            }
        }
        restore = registerForActivityResult(BackupManager.Restore()) {
            it?.let { uri ->
                viewModel.restore(uri)
            }
        }

        bindUi()
    }

    private fun bindUi() {
        // Foreground service
        binding.serviceToggle.isChecked = requireContext().isServiceForegrounded(EventService::class.java)
        binding.serviceToggle.setOnCheckedChangeListener { _, activate ->
            if (activate) {
                startService()
            } else {
                stopService()
            }
        }

        // Wi-Fi SSID
        setWifiSsid()
        binding.wifiTitle.setOnClickListener {
            if (checkPermissions()) {
                with(requireContext()) {
                    startActivity(Intent().settings(this))
                }
            } else {
                permissionsChecker.launch(PERMISSIONS)
            }
        }

        // Google Fi
        binding.fiTitle.setOnClickListener {
            showCodes()
        }

        // Backup
        // Save
        binding.backupSave.setOnClickListener {
            lifecycleScope.launch {
                viewModel.checkpoint()
                save.launch(Unit)
            }
        }
        // Restore
        binding.backupRestore.setOnClickListener {
            restore.launch(Unit)
        }

        // Events
        // Delete
        binding.eventsDelete.setOnClickListener {
            showDelete()
        }
        // Stats
        binding.eventsStats.setOnClickListener {
            StatsFragment().show(parentFragmentManager, StatsFragment.TAG)
        }
        // Legend
        binding.eventsIcons.setOnClickListener {
            IconsFragment().show(parentFragmentManager, IconsFragment.TAG)
        }

        // About
        // Version
        binding.aboutVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        // Help
        binding.aboutHelp.setOnClickListener {
            openBrowserIfInstalled(HELP)
        }
        // Privacy
        binding.aboutPrivacy.setOnClickListener {
            openBrowserIfInstalled(PRIVACY_POLICY)
        }
    }

    private fun setWifiSsid() {
        binding.wifiSubtitle.text = getString(
            if (checkPermissions()) {
                R.string.enabled
            } else {
                R.string.location_permissions
            }
        )
    }

    private fun checkPermissions(): Boolean {
        PERMISSIONS.forEach {
            if (requireContext().checkSelfPermission(it) != PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun startService() {
        val intent = Intent(requireContext(), EventService::class.java).apply {
            putExtra(STARTED_FROM_BOOT_KEY, false)
        }
        requireContext().startService(intent)
    }

    private fun stopService() {
        requireContext().stopService(Intent(requireContext(), EventService::class.java))
    }

    private fun showDelete() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_all)
            .setMessage(R.string.sure)
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.deleteEvents()
            }
            .show()
    }

    private fun showCodes() {
        CodesFragment(all = true).show(parentFragmentManager, CodesFragment.TAG)
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}