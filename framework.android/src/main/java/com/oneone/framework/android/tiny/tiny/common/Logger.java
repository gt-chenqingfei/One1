package com.oneone.framework.android.tiny.tiny.common;

import android.util.Log;

import com.oneone.framework.android.tiny.tiny.Tiny;

/**
 * Created by zhengxiaoyong on 2017/3/11.
 */
public final class Logger {

    private static final String TAG = "tiny";

    public static void e(String message) {
        if (Tiny.getInstance().isDebug())
            Log.e(TAG, message);
    }
}
