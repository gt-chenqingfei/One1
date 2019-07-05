package com.oneone.framework.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Dimension utility
 *
 * @author johnson
 */
public class DimensionUtils {

    /**
     * Convert dip to pixel
     *
     * @param context The android context
     * @param dip     The dip value
     * @return the dimension in pixel
     */
    public static int dip2px(Context context, float dip) {
        final float m = context.getResources().getDisplayMetrics().density;
        return (int) (dip * m + 0.5f);
    }

    public static int getSizeInPixels(float dip, Context context) {
        final float m = context.getResources().getDisplayMetrics().density;
        return (int) (dip * m + 0.5f);
    }

    /**
     * Convert pixel to dip
     *
     * @param context The android context
     * @param px      The pixel value
     * @return the dimension in dip
     */
    public static int px2dip(Context context, float px) {
        final float m = context.getResources().getDisplayMetrics().density;
        return (int) (px / m + 0.5f);
    }

    public static int getWidth(Context context) {
        initScreen(context);
        if (screen != null) {
            return screen.widthPixels;
        }
        return 0;
    }

    public static int getHeight(Context context) {
        initScreen(context);
        if (screen != null) {
            return screen.heightPixels;
        }
        return 0;
    }

    private static void initScreen(Context context) {
        if (screen == null) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            screen = new Screen(dm.widthPixels, dm.heightPixels);
        }
    }

    private DimensionUtils() {
    }

    private static Screen screen = null;

    public static class Screen {
        public int widthPixels;
        public int heightPixels;

        public Screen() {
        }

        public Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }

        @Override
        public String toString() {
            return "(" + widthPixels + "," + heightPixels + ")";
        }

    }

}
