package com.oneone.framework.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectivityUtils {

    /**
     * Returns the active network information
     *
     * @param context The android context
     * @return The active network info or null if no network activated
     */
    public static NetworkInfo getActiveNetwork(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == cm) {
            return null;
        }

        return cm.getActiveNetworkInfo();
    }

    /**
     * Returns the active network type
     *
     * @param context The android context
     * @return The active network type or -1 if no network activated
     */
    public static int getActiveNetworkType(Context context) {
        final NetworkInfo ni = getActiveNetwork(context);
        if (null != ni) {
            return ni.getType();
        }

        return -1; // ConnectivityManager.TYPE_NONE;
    }

    private ConnectivityUtils() {
    }

}
