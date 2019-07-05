package com.oneone.support.share;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class WechatMoments extends ShareBase {
    public static final String NAME = WechatMoments.class.getSimpleName();

    public WechatMoments(Callback callback, Context context) {
        super(callback, context);
    }

    @Override
    public void share(ShareParams params) {
        this.shareParams = params;
        Platform platform = ShareSDK.getPlatform(cn.sharesdk.wechat.moments.WechatMoments.NAME);
        platform.setPlatformActionListener(this);
        Platform.ShareParams shareParams = ShareParamsUtil.convertParam(params, getContext());
        platform.share(shareParams);
    }

    @Override
    public boolean getAuthorized() {
        return false;
    }
}
