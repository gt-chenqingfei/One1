package com.oneone.framework.android.tiny.tiny.callable;

import android.graphics.Bitmap;


import com.oneone.framework.android.tiny.tiny.Tiny;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
abstract class BaseBitmapBatchCompressCallable implements Callable<Bitmap[]> {

    Tiny.BitmapCompressOptions mCompressOptions;

    BaseBitmapBatchCompressCallable(Tiny.BitmapCompressOptions options) {
        mCompressOptions = options;
    }

}
