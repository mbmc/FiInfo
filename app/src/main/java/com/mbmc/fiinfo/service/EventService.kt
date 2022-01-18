package com.mbmc.fiinfo.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.IntentFilter
import android.net.Network
import android.os.*
import android.telephony.*
import android.net.NetworkCapabilities

import android.net.NetworkRequest
import android.net.wifi.WifiManager.*
import android.telephony.TelephonyManager.*
import com.mbmc.fiinfo.helper.ConnectivityManager
import com.mbmc.fiinfo.helper.NotificationManager
import com.mbmc.fiinfo.util.STARTED_FROM_BOOT_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import android.net.ConnectivityManager as AndroidConnectivityManager
import android.telephony.TelephonyCallback as AndroidTelephonyCallback

@AndroidEntryPoint
class EventService : Service() {

    private var isRunning = false
    @Inject lateinit var connectivityManager: ConnectivityManager
    @Inject lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        isRunning = false
        stopForeground(true)
        unsetCallbacks()

        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startFromBoot = intent?.getBooleanExtra(STARTED_FROM_BOOT_KEY, false) ?: false
        if (startFromBoot) {
            connectivityManager.phoneOn()
        }
        start()
        return START_NOT_STICKY
    }

    private fun start() {
        startForeground(NotificationManager.ID, notificationManager.provide())
        isRunning = true
        startTime = System.currentTimeMillis()
        setCallbacks()
    }

    private var telephonyCallback = TelephonyCallback()

    inner class TelephonyCallback : AndroidTelephonyCallback(),
        AndroidTelephonyCallback.DisplayInfoListener,
        AndroidTelephonyCallback.UserMobileDataStateListener {

        override fun onDisplayInfoChanged(telephonyDisplayInfo: TelephonyDisplayInfo) {
            if (tooSoon()) {
                return
            }
            if (telephonyDisplayInfo.networkType != NETWORK_TYPE_UNKNOWN) {
                connectivityManager.updateNetwork(telephonyDisplayInfo)
            }
        }

        override fun onUserMobileDataStateChanged(boolean: Boolean) {
            if (tooSoon()) {
                return
            }
            if (boolean) {
                connectivityManager.cellularOn()
            } else {
                connectivityManager.cellularOff()
            }
        }
    }

    private var receiver = object : BroadcastReceiver() {
        override fun onReceive(content: Context?, intent: Intent?) {
            when (intent?.action) {
                WIFI_STATE_CHANGED_ACTION -> {
                    if (tooSoon()) {
                        return
                    }

                    when (intent.extras?.getInt(EXTRA_WIFI_STATE)) {
                        WIFI_STATE_ENABLED -> connectivityManager.wifiOn()
                        WIFI_STATE_DISABLED -> connectivityManager.wifiOff()
                    }
                }

                ACTION_AIRPLANE_MODE_CHANGED -> {
                    if (intent.extras?.getBoolean("state") == true) {
                        connectivityManager.airplaneOn()
                    } else {
                        connectivityManager.airplaneOff()
                    }
                }

                ACTION_SHUTDOWN -> {
                    connectivityManager.phoneOff()
                }
            }
        }
    }

    private val filter = IntentFilter(ACTION_AIRPLANE_MODE_CHANGED).apply {
        addAction(WIFI_STATE_CHANGED_ACTION)
        addAction(ACTION_SHUTDOWN)
    }

    private var networkCallback =
        object : android.net.ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connectivityManager.addNetwork(network, tooSoon())
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                connectivityManager.removeNetwork(network)
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                connectivityManager.updateNetwork(network, networkCapabilities)
            }
        }

    private fun getNetworkRequest() =
        NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

    private fun setCallbacks() {
        (getSystemService(CONNECTIVITY_SERVICE) as AndroidConnectivityManager)
            .registerNetworkCallback(getNetworkRequest(), networkCallback)

        (getSystemService(TELEPHONY_SERVICE) as TelephonyManager)
            .registerTelephonyCallback(mainExecutor, telephonyCallback)

        registerReceiver(receiver, filter)
    }

    private fun unsetCallbacks() {
        (getSystemService(Context.CONNECTIVITY_SERVICE) as AndroidConnectivityManager)
            .unregisterNetworkCallback(networkCallback)

        (getSystemService(TELEPHONY_SERVICE) as TelephonyManager)
            .unregisterTelephonyCallback(telephonyCallback)

        unregisterReceiver(receiver)
    }

    private fun tooSoon(): Boolean =
        System.currentTimeMillis() - startTime < STICKY_EVENT_THRESHOLD && !startFromBoot

    companion object {
        private var startTime: Long = 0
        private const val STICKY_EVENT_THRESHOLD = 1000 // 1s
        private var startFromBoot = true
    }
}