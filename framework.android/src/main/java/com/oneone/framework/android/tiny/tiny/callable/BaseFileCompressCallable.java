package com.oneone.framework.android.tiny.tiny.callable;

import com.oneone.framework.android.tiny.tiny.Tiny;
import com.oneone.framework.android.tiny.tiny.common.CompressResult;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/1.
 */
abstract class BaseFileCompressCallable implements Callable<CompressResult> {

    Tiny.FileCompressOptions mCompressOptions;

    boolean shouldReturnBitmap;

    BaseFileCompressCallable(Tiny.FileCompressOptions options, boolean withBitmap) {
        mCompressOptions = options;
        shouldReturnBitmap = withBitmap;
    }

}
