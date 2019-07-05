package com.oneone.framework.android.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * Process operating utility
 *
 * @author johnson
 */
public final class ProcessUtils {

    /**
     * Returns the current process name
     *
     * @param ctx The context
     * @return the current process name
     */
    public static String getCurrentProcessName(Context ctx) {
        final ActivityManager am = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (null == am)
            return "";

        final int pid = android.os.Process.myPid();
        if (am.getRunningAppProcesses() == null)
            return "";
        for (final RunningAppProcessInfo rapi : am.getRunningAppProcesses()) {
            if (rapi.pid == pid) {
                return rapi.processName;
            }
        }

        return "";
    }

    private ProcessUtils() {
    }

}
