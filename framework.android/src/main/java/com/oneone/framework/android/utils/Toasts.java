package com.oneone.framework.android.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public final class Toasts {

    private static Toast instance;

    private Toasts() {
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getString(resId), duration);
    }

    public synchronized static void show(Context context, CharSequence text, int duration) {
        if (null == instance) {
            instance = Toast.makeText(context, text, duration);
        } else {
            instance.setText(text);
        }

        instance.show();
    }

    public static void show(Context context, int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void showOnUiThread(final Activity activity, final int resId) {
        if (null == activity) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(activity, resId);
            }
        });
    }


    public static void showOnUiThreadWithResId(final Context context, final int resId) {
        if (null == context) {
            return;
        }
        Handler handler = new Handler(context.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                show(context, resId);
                return false;
            }
        });
        handler.sendEmptyMessage(0);
    }

    public static void showOnUiThreadWithText(final Context context, final String text) {
        if (null == context) {
            return;
        }
        Handler handler = new Handler(context.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                show(context, text);
                return false;
            }
        });
        handler.sendEmptyMessage(0);
    }

    public static void showOnUiThread(final Activity activity, final String text) {
        if (null == activity) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(activity, text);
            }
        });
    }

    public static void show(final Activity activity, final Character text) {
        if (null == activity) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(activity, text);
            }
        });
    }

}
