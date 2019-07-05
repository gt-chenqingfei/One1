package com.oneone.support.share;

import android.content.Context;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.oneone.BuildConfig;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.AssetsUtil;

import cn.sharesdk.framework.Platform;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class ShareParamsUtil {

    public static Platform.ShareParams convertParam(ShareParams params, Context context) {
        Platform.ShareParams param = new Platform.ShareParams();
        param.setTitle(params.getTitle());
        param.setText(params.getText());
        param.setUrl(params.getUrl());
        param.setImageUrl(params.getImageUrl());
        param.setShareType(params.getType());
        return param;
    }


    public static ShareParams getParam4SingleProfile(Context context, String userId) {
        ShareInfo shareInfo = getShareInfo(context);
        String sign = HereUser.getInstance().getLoginInfo().getAccountSign();
        ShareInfo.SharePersonInfo sharePersonInfo = shareInfo.getSingleProfile();
        String url = shareInfo.getShareHost() + sharePersonInfo.getUrl().replace(ShareInfo.KEY_USER_ID, userId).replace(ShareInfo.KEY_SIGN, sign);
        return getBaseParam(sharePersonInfo, url);
    }

    public static ShareParams getParam4MatcherProfile(Context context, String userId, String nickName) {

        ShareInfo shareInfo = getShareInfo(context);
        String sign = HereUser.getInstance().getLoginInfo().getAccountSign();
        ShareInfo.SharePersonInfo sharePersonInfo = shareInfo.getMatcherProfile();
        String url = shareInfo.getShareHost() + sharePersonInfo.getUrl().replace(ShareInfo.KEY_USER_ID,
                userId).replace(ShareInfo.KEY_SIGN, sign);
        ShareParams param = getBaseParam(sharePersonInfo, url);
        param.setTitle(sharePersonInfo.getTitle().replace(ShareInfo.SHARE_INFO_MARCHER_NAME, nickName));
        return param;
    }

    public static ShareParams getParam4SelfMatcherProfile(Context context) {
        ShareInfo shareInfo = getShareInfo(context);
        String sign = HereUser.getInstance().getLoginInfo().getAccountSign();
        ShareInfo.SharePersonInfo sharePersonInfo = shareInfo.getSelfMatcherProfile();
        String url = shareInfo.getShareHost() + sharePersonInfo.getUrl().replace(ShareInfo.KEY_SELF_USER_ID,
                HereUser.getUserId()).replace(ShareInfo.KEY_SIGN, sign);
        return getBaseParam(sharePersonInfo, url);
    }

    public static ShareParams getParam4SelfSingleProfile(Context context) {
        ShareInfo shareInfo = getShareInfo(context);
        String sign = HereUser.getInstance().getLoginInfo().getAccountSign();
        ShareInfo.SharePersonInfo sharePersonInfo = shareInfo.getSelfSingleProfile();
        String url = shareInfo.getShareHost() + sharePersonInfo.getUrl().replace(ShareInfo.KEY_SELF_USER_ID,
                HereUser.getUserId()).replace(ShareInfo.KEY_SIGN, sign);
        return getBaseParam(sharePersonInfo, url);
    }

    public static ShareParams getParam4InviteSingle(Context context) {
        ShareInfo shareInfo = getShareInfo(context);
        String sign = HereUser.getInstance().getLoginInfo().getAccountSign();
        ShareInfo.SharePersonInfo sharePersonInfo = shareInfo.getInviteSingle();
        String url = shareInfo.getShareHost() + sharePersonInfo.getUrl().replace(ShareInfo.KEY_SELF_USER_ID,
                HereUser.getUserId()).replace(ShareInfo.KEY_SIGN, sign);
        return getBaseParam(sharePersonInfo, url);
    }

    public static ShareParams getParam4InviteMatcher(Context mContext) {
        ShareInfo shareInfo = getShareInfo(mContext);
        String sign = HereUser.getInstance().getLoginInfo().getAccountSign();
        String userId =  HereUser.getInstance().getLoginInfo().getUserId();
        ShareInfo.SharePersonInfo sharePersonInfo = shareInfo.getInviteMatcher();
        String url = shareInfo.getShareHost() + sharePersonInfo.getUrl().replace(ShareInfo.KEY_SELF_USER_ID,
                userId).replace(ShareInfo.KEY_SIGN, sign);

        return getBaseParam(sharePersonInfo, url);
    }

    private static ShareParams getBaseParam(ShareInfo.SharePersonInfo sharePersonInfo, String url) {
        ShareParams param = new ShareParams();
        param.setText(sharePersonInfo.getDescription());
        param.setTitle(sharePersonInfo.getTitle());
        param.setUrl(url);
        param.setType(ShareParams.TYPE_WEB_PAGE);
        return param;
    }

    private static ShareInfo getShareInfo(Context context) {
        String json = DefaultSP.getInstance().getString(context, ShareInfo.SHARE_KEY,
                "");
        if (TextUtils.isEmpty(json)) {
            json = AssetsUtil.getContentFromAssets(context, ShareInfo.SHARE_INFO_JSON);
        }
        return new Gson().fromJson(json, ShareInfo.class);
    }


}
