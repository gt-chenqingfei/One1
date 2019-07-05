package com.oneone.support.share;

import android.content.Context;

import com.mob.MobSDK;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * @version v1.0.0
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public abstract class ShareBase implements IShare, PlatformActionListener {
    private Context mContext;
    protected Callback mCallback;
    protected ShareParams shareParams;

    public ShareBase(Callback callback, Context context) {
        this.mCallback = callback;
        this.mContext = context;
        MobSDK.init(context);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (mCallback != null) {
            mCallback.onComplete(shareParams, platform, i, hashMap);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (mCallback != null) {
            mCallback.onError(shareParams, platform, i, throwable);
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (mCallback != null) {
            mCallback.onCancel(shareParams, platform, i);
        }
    }


    public Context getContext()  {
        return mContext;
    }


}
