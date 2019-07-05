package com.oneone.framework.ui.glide;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @since 18/2/6.
 * Copyright © 2018 Here Technology Co.,Ltd. All rights reserved.
 */

public class GetImageCacheTask extends AsyncTask<String, Void, File> {
    private final Context context;
    IOnGetGlideImageCallback mCallback;

    public interface IOnGetGlideImageCallback {
        void onGetGlideCachePath(String glidePath);
        void onGetGlideCacheFile(File glideFile);
    }

    public GetImageCacheTask(Context context, IOnGetGlideImageCallback imageCallback) {
        this.context = context;
        this.mCallback = imageCallback;
    }

    @Override
    protected File doInBackground(String... params) {
        String imgUrl = params[0];
        try {
            return Glide.with(context)
                    .load(imgUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        //此path就是对应文件的缓存路径
        String cachePath = result.getPath();
        cachePath = cachePath.substring(0, cachePath.lastIndexOf(".")) + ".png";
        if (mCallback != null) {
            mCallback.onGetGlideCachePath(cachePath);
            mCallback.onGetGlideCacheFile(result);
        } else {
            LoggerFactory.getLogger(getClass()).error("GetImageCacheTask mCallback is null!");
        }
    }
}
