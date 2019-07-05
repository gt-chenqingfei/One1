package com.oneone.modules.profile;

import android.content.Context;
import android.content.Intent;

import com.oneone.api.constants.Role;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.modules.user.util.HereUserUtil;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ProfileStater {
    public static final String TAB_TIMELINE ="timeline";
    public static final String TAB_SINGLES ="singles";
    public static final String TAB_STORY ="story";

    public static void startWithUserInfo(Context context, UserInfoBase info) {
        if (info == null) {
            return;
        }
        UserInfo userInfo = HereUserUtil.cloneUserInfoBase2UserInfo(info);
        startWithUserInfo(context, userInfo,null);
    }

    public static void startWithUserInfo(Context context, UserInfo userInfo,String tab) {
        if (userInfo == null) {
            return;
        }
        Intent intent = new Intent();

        if (userInfo.getRole() == Role.MATCHER) {
            intent.setClass(context, Profile4MatcherActivity.class);
        } else {
            intent.setClass(context, Profile4SingleActivity.class);
        }

        intent.putExtra(AbstractProfileActivity.EXTRA_TAB, tab);
        intent.putExtra(AbstractProfileActivity.EXTRA_USER_INFO, userInfo);
        intent.putExtra(AbstractProfileActivity.EXTRA_USER_ID, userInfo.getUserId());

        context.startActivity(intent);
    }

    public static void startWithUserInfo(Context context, UserInfo userInfo) {
       startWithUserInfo(context,userInfo,null);
    }
}
