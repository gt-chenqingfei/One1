package com.oneone.framework.android.tiny.tiny.callback;

import android.graphics.Bitmap;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public interface FileWithBitmapCallback extends Callback {

    void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t);

}
