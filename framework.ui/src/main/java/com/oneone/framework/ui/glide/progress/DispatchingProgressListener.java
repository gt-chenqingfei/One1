package com.oneone.framework.ui.glide.progress;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;


/**
 * @since 17/12/20.
 * Copyright © 2017 Here Technology Co.,Ltd. All rights reserved.
 */

public class DispatchingProgressListener implements ResponseProgressListener {
    private static final Map<String, UIProgressListener> LISTENERS = new HashMap<>();
    private static final Map<String, Long> PROGRESSES = new HashMap<>();

    private final Handler handler;

    public DispatchingProgressListener() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public static void forget(String url) {
        LISTENERS.remove(url);
        PROGRESSES.remove(url);
    }

    public static void expect(String url, UIProgressListener listener) {
        LISTENERS.put(url, listener);
    }

    @Override
    public void update(HttpUrl url, final long bytesRead, final long contentLength) {
        //System.out.printf("%s: %d/%d = %.2f%%%n", url, bytesRead, contentLength, (100f * bytesRead) / contentLength);
        String key = url.toString();
        final UIProgressListener listener = LISTENERS.get(key);
        if (listener == null) {
            return;
        }
        if (contentLength <= bytesRead) {
            forget(key);
        }
        if (needsDispatch(key, bytesRead, contentLength, listener.getGranularityPercentage())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onProgress(bytesRead, contentLength);
                }
            });
        }
    }

    private boolean needsDispatch(String key, long current, long total, float granularity) {
        if (granularity == 0 || current == 0 || total == current) {
            return true;
        }
        float percent = 100f * current / total;
        long currentProgress = (long) (percent / granularity);
        Long lastProgress = PROGRESSES.get(key);
        if (lastProgress == null || currentProgress != lastProgress) {
            PROGRESSES.put(key, currentProgress);
            return true;
        } else {
            return false;
        }
    }

}
