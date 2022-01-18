package com.mbmc.fiinfo.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.databinding.ActivityMainBinding
import com.mbmc.fiinfo.util.settings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionsChecker: ActivityResultLauncher<String>
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var root: View
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        root = binding.root
        setContentView(binding.root)

        permissionsChecker = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            checkRationale(it)
        }

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_CANCELED) {
                checkPermissions()
            }
        }

        checkPermissions()
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp()

    private fun checkPermissions() {
        if (checkSelfPermission(PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            permissionsChecker.launch(PERMISSION)
        } else {
            bindUi()
        }
    }

    private fun checkRationale(granted: Boolean) {
        if (!granted) {
            val rationale = shouldShowRequestPermissionRationale(PERMISSION)
            val reason =
                if (!rationale) {
                    R.string.permissions_require_settings
                } else {
                    R.string.permissions_required
                }
            Snackbar.make(
                root,
                reason,
                Snackbar.LENGTH_INDEFINITE
            )
                .apply {
                    setAction(R.string.ok) {
                        if (rationale) {
                            permissionsChecker.launch(PERMISSION)
                        } else {
                            startForResult.launch(Intent().settings(this@MainActivity))
                        }
                    }
                }
                .show()
        } else {
            bindUi()
        }
    }

    private fun bindUi() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host) as NavHostFragment
        navController = navHostFragment.navController
        navController.graph = navController.navInflater.inflate(R.navigation.graph)
        setupActionBarWithNavController(navController)
    }

    companion object {
        private const val PERMISSION = Manifest.permission.READ_PHONE_STATE
    }
}