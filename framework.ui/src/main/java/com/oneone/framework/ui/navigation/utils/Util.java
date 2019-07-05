package com.oneone.framework.ui.navigation.utils;

import android.content.res.Resources;

public class Util {
    /**
     * this function get dp based value and convert it to px
     * @param dp value based on dp metric
     * @return return value based on px metric
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
