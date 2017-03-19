package com.mbmc.fiinfo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.mbmc.fiinfo.R;
import com.mbmc.fiinfo.data.ConnectivityEvent;
import com.mbmc.fiinfo.data.Event;
import com.mbmc.fiinfo.data.MobileCarrier;


public final class ConnectivityUtil {

    private static String UNKNOWN = "Unknown";


    public static ConnectivityEvent getConnectivityEvent(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    if (isConnectedToWifi(context)) {
                        return new ConnectivityEvent(Event.WIFI_MOBILE, getWifiName(context),
                                getMobileName(context), getSpeed(networkInfo.getSubtype()));
                    }
                    return new ConnectivityEvent(Event.MOBILE, getMobileName(context), getSpeed(networkInfo.getSubtype()));

                case ConnectivityManager.TYPE_WIFI:
                    String speed = getSpeedIfAny(context);
                    if (!speed.equals(UNKNOWN)) {
                        String mobile = getMobileName(context);
                        if (!mobile.equals(UNKNOWN)) {
                            return new ConnectivityEvent(Event.WIFI_MOBILE, getWifiName(context),
                                    mobile, speed);
                        }
                    }
                    return new ConnectivityEvent(Event.WIFI, getWifiName(context));
            }
        }

        return new ConnectivityEvent(Event.DISCONNECT);
    }

    // As listed in TelephonyManager.java
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
                return UNKNOWN;
        }
    }

    /*
        ServiceState.java
        public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;
        public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;
        public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;
        public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;
        public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;
        public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;
        public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;
        public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;
        public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;
        public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;
        public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;
        public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;
        public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;
        public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;
        public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;
        public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;

        case ServiceState.RIL_RADIO_TECHNOLOGY_GPRS:
            return TelephonyManager.NETWORK_TYPE_GPRS;
        case ServiceState.RIL_RADIO_TECHNOLOGY_EDGE:
            return TelephonyManager.NETWORK_TYPE_EDGE;
        case ServiceState.RIL_RADIO_TECHNOLOGY_UMTS:
            return TelephonyManager.NETWORK_TYPE_UMTS;
        case ServiceState.RIL_RADIO_TECHNOLOGY_HSDPA:
            return TelephonyManager.NETWORK_TYPE_HSDPA;
        case ServiceState.RIL_RADIO_TECHNOLOGY_HSUPA:
            return TelephonyManager.NETWORK_TYPE_HSUPA;
        case ServiceState.RIL_RADIO_TECHNOLOGY_HSPA:
            return TelephonyManager.NETWORK_TYPE_HSPA;
        case ServiceState.RIL_RADIO_TECHNOLOGY_IS95A:
        case ServiceState.RIL_RADIO_TECHNOLOGY_IS95B:
            return TelephonyManager.NETWORK_TYPE_CDMA;
        case ServiceState.RIL_RADIO_TECHNOLOGY_1xRTT:
            return TelephonyManager.NETWORK_TYPE_1xRTT;
        case ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_0:
            return TelephonyManager.NETWORK_TYPE_EVDO_0;
        case ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_A:
            return TelephonyManager.NETWORK_TYPE_EVDO_A;
        case ServiceState.RIL_RADIO_TECHNOLOGY_EVDO_B:
            return TelephonyManager.NETWORK_TYPE_EVDO_B;
        case ServiceState.RIL_RADIO_TECHNOLOGY_EHRPD:
            return TelephonyManager.NETWORK_TYPE_EHRPD;
        case ServiceState.RIL_RADIO_TECHNOLOGY_LTE:
            return TelephonyManager.NETWORK_TYPE_LTE;
        case ServiceState.RIL_RADIO_TECHNOLOGY_HSPAP:
            return TelephonyManager.NETWORK_TYPE_HSPAP;
        default:
            return TelephonyManager.NETWORK_TYPE_UNKNOWN;
     */
    public static String getSpeedFromService(int type) {
        int speed;
        switch (type) {
            case 0: speed = TelephonyManager.NETWORK_TYPE_UNKNOWN; break;
            case 1: speed = TelephonyManager.NETWORK_TYPE_GPRS; break;
            case 2: speed = TelephonyManager.NETWORK_TYPE_EDGE; break;
            case 3: speed = TelephonyManager.NETWORK_TYPE_UMTS; break;
            case 4: speed = TelephonyManager.NETWORK_TYPE_CDMA; break;
            case 5: speed = TelephonyManager.NETWORK_TYPE_CDMA; break;
            case 6: speed = TelephonyManager.NETWORK_TYPE_1xRTT; break;
            case 7: speed = TelephonyManager.NETWORK_TYPE_EVDO_0; break;
            case 8: speed = TelephonyManager.NETWORK_TYPE_EVDO_A; break;
            case 9: speed = TelephonyManager.NETWORK_TYPE_HSDPA; break;
            case 10: speed = TelephonyManager.NETWORK_TYPE_HSUPA; break;
            case 11: speed = TelephonyManager.NETWORK_TYPE_HSPA; break;
            case 12: speed = TelephonyManager.NETWORK_TYPE_EVDO_B; break;
            case 13: speed = TelephonyManager.NETWORK_TYPE_EHRPD; break;
            case 14: speed = TelephonyManager.NETWORK_TYPE_LTE; break;
            case 15: speed = TelephonyManager.NETWORK_TYPE_GPRS; break;
            default: speed = TelephonyManager.NETWORK_TYPE_HSPAP; break;
        }
        return getSpeed(speed);
    }

    public static String getMobileName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return UNKNOWN;
        }

        return MobileCarrier.getName(context, telephonyManager.getSimOperator(), telephonyManager.getSimOperatorName());
    }

    public static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    public static String getSpeedIfAny(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return UNKNOWN;
        }

        return getSpeed(telephonyManager.getNetworkType());
    }


    private ConnectivityUtil() {

    }

}
