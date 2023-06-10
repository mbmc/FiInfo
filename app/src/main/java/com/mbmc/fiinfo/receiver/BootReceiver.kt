package com.mbmc.fiinfo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.mbmc.fiinfo.helper.PreferenceManager
import com.mbmc.fiinfo.service.EventService
import com.mbmc.fiinfo.util.STARTED_FROM_BOOT_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!(intent?.action == ACTION_BOOT_COMPLETED && preferenceManager.doesStartOnBoot())) {
            return
        }
        context?.apply {
            startService(
                Intent(this, EventService::class.java).apply {
                    putExtra(STARTED_FROM_BOOT_KEY, true)
                }
            )
        }
    }
}