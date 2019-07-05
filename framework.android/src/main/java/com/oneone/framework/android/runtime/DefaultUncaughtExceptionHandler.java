package com.oneone.framework.android.runtime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.oneone.framework.android.BuildConfig;
import com.oneone.framework.android.utils.ActivityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultUncaughtExceptionHandler.class);

    private UncaughtExceptionHandler handler;

    private Context mContext;

    public DefaultUncaughtExceptionHandler(Context context) {
        this.mContext = context;
        this.handler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable t) {
        logger.error(t.getMessage(), t);

        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            return; // ignore network error
        }

        if (null == this.handler) {
            return;
        }

        if (BuildConfig.DEBUG) {
            this.handler.uncaughtException(thread, t);
            return;
        }

        Intent intent = new Intent();
        intent.setClassName(mContext.getApplicationContext(), ActivityUtils.getLauncherActivity(mContext.getPackageName()));
        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        //重启应用，得使用PendingIntent
        PendingIntent restartIntent = PendingIntent.getActivity(
                mContext.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent); // 重启应用

        // 结束应用
        ActivityUtils.finishAllActivities();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
