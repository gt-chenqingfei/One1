package com.oneone.support.share;

import android.content.Context;
import android.text.TextUtils;

import com.oneone.BuildConfig;
import com.oneone.Constants;
import com.oneone.OneOne;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.framework.android.preference.PacketSP;

import java.util.Random;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 Here Technology Co.,Ltd. All rights reserved.
 */

public class Wechat extends ShareBase {
    public static final String NAME = Wechat.class.getSimpleName();

    public Wechat(Callback callback, Context context) {
        super(callback, context);
    }

    @Override
    public void share(ShareParams params) {
        this.shareParams = params;
        Platform platform = ShareSDK.getPlatform(cn.sharesdk.wechat.friends.Wechat.NAME);
        platform.setPlatformActionListener(this);
        Platform.ShareParams shareParams = ShareParamsUtil.convertParam(params, getContext());
        platform.share(shareParams);
    }

    @Override
    public boolean getAuthorized() {
        boolean isValid = PlatformUtil.isWechatValid(getContext());
        if (!isValid) {
            return false;
        }
        if (BuildConfig.DEBUG) {
            getAuthorizedDebug();
        } else {
            getAuthorizedRelease();
        }
        return true;
    }

    private void getAuthorizedRelease() {
        Platform platform = ShareSDK.getPlatform(cn.sharesdk.wechat.friends.Wechat.NAME);
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
            ShareSDK.removeCookieOnAuthorize(true);
        }
        platform.SSOSetting(false);
        platform.setPlatformActionListener(this);
        platform.showUser(null);
    }

    private void getAuthorizedDebug() {
        Platform platform = new cn.sharesdk.wechat.friends.Wechat();
        String userId = (String) PacketSP.getInstance().get(OneOne.getInstance(), Constants.PREF.PREF_WECHAT_DEBUG_TOKEN, "");
        if (TextUtils.isEmpty(userId)) {
            userId = getRandomString();
            PacketSP.getInstance().put(OneOne.getInstance(), Constants.PREF.PREF_WECHAT_DEBUG_TOKEN, userId).apply();
        }
        platform.getDb().putUserId(userId);
        mCallback.onComplete(shareParams, platform, 0, null);
    }

    private static String getRandomString() {
        String str = "fabt8f35as1iend5016045de7a3a3d2b175032904fdf9";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 46; ++i) {
            int number = random.nextInt(40);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
