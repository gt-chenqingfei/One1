package com.oneone.utils;

import android.text.TextUtils;

/**
 * Created by here on 18/7/11.
 */

public class MyTextUtil {
    public static String getLimitEllipseText (String textStr, int limit) {
        if (textStr != null) {
            if (textStr.length() > limit) {
                textStr = textStr.substring(0, limit - 1) + "...";
            }
            return textStr;
        } else {
            return "";
        }
    }
}
