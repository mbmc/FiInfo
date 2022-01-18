package com.mbmc.fiinfo.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import com.mbmc.fiinfo.data.*
import com.mbmc.fiinfo.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

import android.net.ConnectivityManager as AndroidConnectivityManager

@Singleton
class ConnectivityManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventManager: EventManager
) {

    private var networks = mutableMapOf<String, Event>()

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
                Log.d(DEBUG_INFO, "telephonyDisplayInfo: $telephonyDisplayInfo")
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

        Log.d(DEBUG_INFO, "onCapabilitiesChanged: $networkCapabilities")
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            val wifiInfo = networkCapabilities.transportInfo as WifiInfo
            val currentSsid = networks[network.toString()]?.ssid
            val ssid = wifiInfo.ssid
            networks[network.toString()]?.frequency = wifiInfo.getDetails()
            // Only update the SSID if it's different
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED && currentSsid != ssid) {
                Log.d(DEBUG_INFO, "updating wifi from $currentSsid to $ssid")
                networks[network.toString()]?.ssid = ssid
                // new ssid will be displayed on the next event
            }
        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            networks[network.toString()] = telephonyManager.toEvent()
            telephonyDetails()
        }
    }

    fun addNetwork(network: Network, noLog: Boolean = false) {
        updateNetwork(network, true, noLog)
    }

    fun removeNetwork(network: Network) {
        updateNetwork(network, false)
    }

    @SuppressLint("MissingPermission")
    private fun updateNetwork(network: Network, add: Boolean, noLog: Boolean = false) {
        // TODO: fetch everytime?
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as AndroidConnectivityManager
        wifiManager = context.getSystemService(Activity.WIFI_SERVICE) as WifiManager
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val capabilities = connectivityManager.getNetworkCapabilities(network)

        if (!add) {
            Log.d(DEBUG_INFO, "remove $network $networks")
            networks.remove(network.toString())
            Log.d(DEBUG_INFO, "remove $networks")
        } else {
            if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                Log.d(DEBUG_INFO, "has wifi transport")
                val wifiInfo = wifiManager.connectionInfo
                Log.d(DEBUG_INFO, "connection has wifi state: ${wifiInfo.supplicantState}")
                if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                    networks[network.toString()] = Event(
                        type = Type.WIFI,
                        ssid = wifiInfo.ssid,
                        frequency = wifiInfo.getDetails()
                    )
                }
            } else if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) {
                telephonyDetails()
                networks[network.toString()] = telephonyManager.toEvent()
            }
        }

        Log.d(DEBUG_INFO, "networks: $networks")
        Log.d(DEBUG_INFO, "callback capabilities: $capabilities")

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
            DEBUG_INFO, "cellular update ${telephonyManager.simCarrierIdName},"
                    + " ${telephonyManager.simOperator}" //
                    + ", ${telephonyManager.simOperatorName}"
                    + ", ${telephonyManager.networkOperator}" //
                    + ", ${telephonyManager.networkOperatorName}"
        )
    }
}