package com.oneone.framework.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.oneone.framework.android.ApplicationContext;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public final class ActivityUtils {


    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;


    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    static WeakReference<Activity> sTopActivityWeakRef;
    static List<Activity> sActivityList = new LinkedList<>();

    public static void addActivity(Activity activity) {
        sActivityList.add(activity);
        setTopActivityWeakRef(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    public static void setTopActivityWeakRef(Activity activity) {
        if (sTopActivityWeakRef == null || !activity.equals(sTopActivityWeakRef.get())) {
            sTopActivityWeakRef = new WeakReference<>(activity);
        }
    }


    /**
     * 获取launcher activity
     *
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(final String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = ApplicationContext.getInstance().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo aInfo : info) {
            if (aInfo.activityInfo.packageName.equals(packageName)) {
                return aInfo.activityInfo.name;
            }
        }
        return "no " + packageName;
    }

    /**
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public static Activity getTopActivity() {
        if (sTopActivityWeakRef != null) {
            Activity activity = sTopActivityWeakRef.get();
            if (activity != null) {
                return activity;
            }
        }
        List<Activity> activities = sActivityList;
        int size = activities.size();
        return size > 0 ? activities.get(size - 1) : null;
    }

    public static void finishAllActivities() {
        List<Activity> activityList = sActivityList;
        for (int i = activityList.size() - 1; i >= 0; --i) {
            activityList.get(i).finish();
        }
    }

    private static Context getActivityOrApp() {
        Activity topActivity = getTopActivity();
        return topActivity == null ? ApplicationContext.getInstance() : topActivity;
    }
}