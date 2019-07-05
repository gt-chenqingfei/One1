package com.oneone.support.share;

import android.content.Context;

import com.mob.MobSDK;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class PlatformUtil {
    public static boolean isClientValid(String name, Context context) {
        MobSDK.init(context);
        Platform platform = ShareSDK.getPlatform(name);
        if (platform != null) {
            return platform.isClientValid();
        }
        return false;
    }


    public static boolean isWechatValid(Context context) {
        return isClientValid(cn.sharesdk.wechat.friends.Wechat.NAME, context);
    }


}
