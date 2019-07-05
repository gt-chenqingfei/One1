package com.oneone.framework.android.schedule;

import android.content.Context;

public class AsyncTaskQueueFactory {

    private AsyncTaskQueueFactory() {
    }

    public static AsyncTaskQueue newTaskQueue(Context ctx) {
        return new AsyncTaskQueue(ctx);
    }

}
