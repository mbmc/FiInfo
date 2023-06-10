package com.mbmc.fiinfo.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.telephony.TelephonyManager
import android.util.Log
import android.net.wifi.WifiManager
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NONE
import android.telephony.TelephonyManager.NETWORK_TYPE_UNKNOWN
import android.widget.Toast
import com.mbmc.fiinfo.R
import com.mbmc.fiinfo.data.*
import com.mbmc.fiinfo.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

import android.net.ConnectivityManager as AndroidConnectivityManager

@Singleton
class ConnectivityManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventManager: EventManager
) {

    private var networks = ConcurrentHashMap<String, Event>()

    private lateinit var wifiManager: WifiManager
    private lateinit var connectivityManager: AndroidConnectivityManager
    private lateinit var telephonyManager: TelephonyManager

    fun airplaneOn() {
        eventManager.log(Event(Type.AIRPLANE_ON))
    }

    fun airplaneOff() {
        eventManager.log(Event(Type.AIRPLANE_OFF))
    }

    fun phoneOn() {
        eventManager.log(Event(Type.PHONE_ON))
    }

    fun phoneOff() {
        eventManager.log(Event(Type.PHONE_OFF))
    }

    fun cellularOn() {
        eventManager.log(Event(Type.CELLULAR_ON))
    }

    fun cellularOff() {
        eventManager.log(Event(Type.CELLULAR_OFF))
    }

    fun wifiOn() {
        eventManager.log(Event(Type.WIFI_ON))
    }

    fun wifiOff() {
        eventManager.log(Event(Type.WIFI_OFF))
    }

    fun updateNetwork(telephonyDisplayInfo: TelephonyDisplayInfo) {
        if (telephonyDisplayInfo.networkType == NETWORK_TYPE_UNKNOWN) {
            return
        }

        // TODO: filtering?
        networks.forEach { (_, event) ->
            if (event.type == Type.CARRIER) {
                event.speed = telephonyDisplayInfo.networkType
                if (telephonyDisplayInfo.overrideNetworkType != OVERRIDE_NETWORK_TYPE_NONE) {
                    updateSpeedOverride(event, telephonyDisplayInfo.overrideNetworkType)
                }
                Log.d(DEBUG_TAG, "telephonyDisplayInfo: $telephonyDisplayInfo")
                logEvent()
            }
        }
    }

    private fun updateSpeedOverride(event: Event, override: Int) {
        event.speed = override + OVERRIDE_SHIFT
    }

    fun updateNetwork(network: Network, networkCapabilities: NetworkCapabilities) {
        if (!this::connectivityManager.isInitialized) {
            return
        }

        // TODO: check why there's an NPE, sometimes
        if (!(network != null && networkCapabilities != null)) {
            return
        }

        Log.d(DEBUG_TAG, "onCapabilitiesChanged: $networkCapabilities")
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            val wifiInfo = networkCapabilities.transportInfo as WifiInfo
            val currentSsid = networks[network.toString()]?.ssid
            val ssid = wifiInfo.fixSsid()
            networks[network.toString()]?.frequency = wifiInfo.getDetails()
            // Only update the SSID if it's different
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED && currentSsid != ssid) {
                Log.d(DEBUG_TAG, "updating wifi from $currentSsid to $ssid")
                networks[network.toString()]?.ssid = ssid
                // new ssid will be displayed on the next event
            }
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            updateCellular(network)
        }
    }

    fun addNetwork(network: Network, noLog: Boolean = false) {
        updateNetwork(network, true, noLog)
    }

    fun removeNetwork(network: Network) {
        updateNetwork(network, false)
    }

    private fun updateCellular(network: Network) {
        if (context.checkSelfPermission(PHONE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, R.string.permission_required, Toast.LENGTH_SHORT).show()
            return
        }

        networks[network.toString()] = telephonyManager.toEvent()
        telephonyDetails()
    }

    private fun updateNetwork(network: Network, add: Boolean, noLog: Boolean = false) {
        // TODO: fetch everytime?
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as AndroidConnectivityManager
        wifiManager = context.getSystemService(Activity.WIFI_SERVICE) as WifiManager
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val capabilities = connectivityManager.getNetworkCapabilities(network)

        if (!add) {
            Log.d(DEBUG_TAG, "remove $network $networks")
            networks.remove(network.toString())
            Log.d(DEBUG_TAG, "remove $networks")
        } else {
            if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                Log.d(DEBUG_TAG, "has wifi transport")
                val wifiInfo = wifiManager.connectionInfo
                Log.d(DEBUG_TAG, "connection has wifi state: ${wifiInfo.supplicantState}")
                if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                    networks[network.toString()] = Event(
                        type = Type.WIFI,
                        ssid = wifiInfo.fixSsid(),
                        frequency = wifiInfo.getDetails()
                    )
                }
            } else if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) {
                updateCellular(network)
            }
        }

        Log.d(DEBUG_TAG, "networks: $networks, noLog? $noLog")
        Log.d(DEBUG_TAG, "callback capabilities: $capabilities")

        if (noLog) {
            return
        }
        logEvent()
    }

    private fun logEvent() {
        // Disconnected
        if (networks.isEmpty()) {
            eventManager.log(Event(Type.DISCONNECTED))
            return
        }

        // Only 1 connection (wi-fi or cellular)
        if (networks.size == 1) {
            networks.firstNotNullOf {
                eventManager.log(it.value)
            }
            return
        }

        // Both connected
        val event = Event(Type.WIFI_CARRIER)

        networks.forEach { (_, it) ->
            when (it.type) {
                Type.WIFI -> {
                    event.ssid = it.ssid
                    event.frequency = it.frequency
                }

                Type.CARRIER -> {
                    event.mccmnc = it.mccmnc
                    event.operator = it.operator
                    event.speed = it.speed
                }
            }
        }

        eventManager.log(event)
    }

    private fun telephonyDetails() {
        Log.d(
            DEBUG_TAG, "cellular update ${telephonyManager.simCarrierIdName},"
                    + " ${telephonyManager.simOperator}" //
                    + ", ${telephonyManager.simOperatorName}"
                    + ", ${telephonyManager.networkOperator}" //
                    + ", ${telephonyManager.networkOperatorName}"
        )
    }
}