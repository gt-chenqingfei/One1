package com.oneone.framework.android.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public final class PackageUtils {

    private static final Logger logger = LoggerFactory.getLogger(PackageUtils.class);

    /**
     * Install the specified package
     *
     * @param context The android context
     * @param file    The package file to be installed
     */
    public static final void installPackage(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * Returns the version code
     *
     * @param context
     * @return
     */
    public static final int getVersionCode(Context context) {
        final String pkg = context.getPackageName();
        final PackageManager pm = context.getPackageManager();

        try {
            return pm.getPackageInfo(pkg, 0).versionCode;
        } catch (NameNotFoundException e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Returns the version name
     *
     * @param context
     * @return
     */
    public static final String getVersionName(Context context) {
        final String pkg = context.getPackageName();
        final PackageManager pm = context.getPackageManager();

        try {
            return pm.getPackageInfo(pkg, 0).versionName;
        } catch (NameNotFoundException e) {
            logger.error(e.getMessage(), e);
            return "1.0.0";
        }
    }

    /**
     * 获取渠道号
     *
     * @param ctx
     * @return
     */
    public static String getAppDefaultMetaData(Context ctx) {
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    private PackageUtils() {
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                logger.info("Application Background is true");
                return true;
            }
        }

        logger.info("Application Background is false");
        return false;
    }

    public static String getTopActivityName(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (null != topActivity) {
                return topActivity.getClassName();
            }
        }
        return "";
    }

    /**
     * Kill app and restart
     */
    public static void reStart(Context context) {
        logger.info("ReStart application!");
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        Process.killProcess(Process.myPid());
        System.exit(0);
    }

}
