package com.oneone.framework.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public  class TelephonyUtils {

    private TelephonyUtils() {
    }

    /**
     * Returns the device id
     *
     * @param context
     * @return the device id
     */
    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm)
            return null;

        return tm.getDeviceId();
    }

    /**
     * Returns the SIM serial number
     *
     * @param context
     * @return the SIM serial number
     */
    public static String getSimSerialNumber(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm)
            return null;

        return tm.getSimSerialNumber();
    }

    /**
     * Returns a constant indicating the state of the device SIM card.
     *
     * @param context
     * @return
     */
    public static int getSimState(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm)
            return TelephonyManager.SIM_STATE_UNKNOWN;

        return tm.getSimState();
    }

    /**
     * Returns a constant indicating the device phone type. This indicates the
     * type of radio used to transmit voice calls.
     *
     * @param context
     * @return
     */
    public static int getPhoneType(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm)
            return TelephonyManager.PHONE_TYPE_NONE;

        return tm.getPhoneType();
    }

    /**
     * Returns the network type for current data connection
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm)
            return TelephonyManager.NETWORK_TYPE_UNKNOWN;

        return tm.getNetworkType();
    }

}
