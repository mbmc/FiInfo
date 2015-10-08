package com.mbmc.fiinfo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.data.MobileCarrier;


public class ConnectivityUtil {

    public static ConnectivityEvent getConnectivityEvent(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return new ConnectivityEvent(Event.DISCONNECT);
        }

        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_MOBILE:
                return new ConnectivityEvent(Event.MOBILE, getMobileName(context), getSpeed(networkInfo.getSubtype()));

            case ConnectivityManager.TYPE_WIFI:
                return new ConnectivityEvent(Event.WIFI, getWifiName(context));
        }

        return null;
    }

    public static ConnectivityEvent getMobileConnectivityIfAny(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (Network network : connectivityManager.getAllNetworks()) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return new ConnectivityEvent(Event.MOBILE, getMobileName(context), getSpeed(networkInfo.getSubtype()));
                }
            }
        }
        return new ConnectivityEvent(Event.MOBILE, getMobileName(context), getSpeedIfAny(context));
    }

    // As listed in TelephonyManager
    public static String getSpeed(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:  return "2G (1xRTT)";
            case TelephonyManager.NETWORK_TYPE_EDGE:   return "2G (EDGE)";
            case TelephonyManager.NETWORK_TYPE_GPRS:   return "2G (GPRS)";
            case TelephonyManager.NETWORK_TYPE_CDMA:   return "2G (CDMA)";
            case TelephonyManager.NETWORK_TYPE_IDEN:   return "2G (IDEN)";

            case TelephonyManager.NETWORK_TYPE_EHRPD:  return "3G (EHRDP)";
            case TelephonyManager.NETWORK_TYPE_EVDO_0: return "3G (EVDO_0)";
            case TelephonyManager.NETWORK_TYPE_EVDO_A: return "3G (EVDO_A)";
            case TelephonyManager.NETWORK_TYPE_EVDO_B: return "3G (EVDO_B)";
            case TelephonyManager.NETWORK_TYPE_HSDPA:  return "3G (HSDPA)";
            case TelephonyManager.NETWORK_TYPE_HSPA:   return "3G (HSPA)";
            case TelephonyManager.NETWORK_TYPE_HSPAP:  return "3G (HSPAP)";
            case TelephonyManager.NETWORK_TYPE_HSUPA:  return "3G (HDUPA)";
            case TelephonyManager.NETWORK_TYPE_UMTS:   return "3G (UMTS)";

            case TelephonyManager.NETWORK_TYPE_LTE:    return "4G (LTE)";

            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return "Unknown";
        }
    }

    public static String getMobileName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "";
        }

        String sim = telephonyManager.getSimOperator();
        if (MobileCarrier.SPRINT.contains(sim)) {
            return context.getString(R.string.carrier_sprint);
        } else if (MobileCarrier.T_MOBILE.contains(sim)) {
            return context.getString(R.string.carrier_t_mobile);
        }
        return telephonyManager.getSimOperatorName();
    }


    private static String getSpeedIfAny(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "Unknown";
        }

        return getSpeed(telephonyManager.getNetworkType());
    }

    private static String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

}
