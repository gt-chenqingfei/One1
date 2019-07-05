package com.oneone.framework.ui.upgrade.utils;

import android.util.Log;

import com.oneone.framework.ui.upgrade.core.AllenChecker;

/**
 * Created by allenliu on 2017/8/16.
 */

public class ALog {
    public static void e(String msg) {
        if (AllenChecker.isDebug()) {
            if (msg != null && !msg.isEmpty())
                Log.e("Allen Checker", msg);
        }
    }
}
