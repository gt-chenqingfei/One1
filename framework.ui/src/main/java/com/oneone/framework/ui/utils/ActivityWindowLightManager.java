package com.oneone.framework.ui.utils;

import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import static android.content.ContentValues.TAG;

/**
 * Created by qingfei.chen on 17/11/9.
 */

public class ActivityWindowLightManager {


    /**
     * 当前屏幕亮度的模式
     */
    private static int screenMode;

    /**
     * 当前屏幕亮度值 0--255
     */
    private static int screenBrightness;


    public static void changeWindowLight(Activity activity, boolean isNight) {
        try {
                    /*
                     * 获得当前屏幕亮度的模式
                     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
                     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
                     */
            screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            Log.i(TAG, "screenMode = " + screenMode);

            // 获得当前屏幕亮度值 0--255
            screenBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Log.i(TAG, "screenBrightness = " + screenBrightness);

            if (isNight) {
                // 如果当前的屏幕亮度调节调节模式为自动调节，则改为手动调节屏幕亮度
                if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    setScreenMode(activity, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                }
            }

            // 设置屏幕亮度值为最大值255
            setScreenBrightness(activity, isNight ? 0F : screenBrightness);
            if (!isNight) {
                setScreenMode(activity, screenMode);
            }

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private static void setScreenMode(Activity activity, int value) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    private static void setScreenBrightness(Activity activity, float value) {
        Window mWindow = activity.getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        float f = value / 255.0F;
        mParams.screenBrightness = f;
        mWindow.setAttributes(mParams);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }


}
