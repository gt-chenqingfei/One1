package com.oneone.schema.filter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.oneone.api.constants.Role;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.activity.ModifyMatcherUserBasicActivity;
import com.oneone.modules.user.activity.ModifySingleUserBasicActivity;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.schema.interceptor.IInterceptorHandler;

/**
 * @author qingfei.chen
 * @since 2018/7/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ProfileInfoFilter extends BaseFilter {
    @Override
    public boolean process(Context context, Postcard postcard, InterceptorCallback callback) {
        if (super.process(context, postcard, callback)) {
            return true;
        }
        String userId = postcard.getUri().getQueryParameter("userId");
        String role = postcard.getUri().getQueryParameter("role");
        String tab = postcard.getUri().getQueryParameter("tab");
        boolean self = postcard.getUri().getBooleanQueryParameter("self", false);

        if (self) {
            ProfileStater.startWithUserInfo(context,  HereUser.getInstance().getUserInfo(), tab);
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            int roleValue = 0;
            switch (role) {
                case "matcher":
                    roleValue = Role.MATCHER;
                    break;
                case "single":
                    roleValue = Role.SINGLE;
                    break;
            }
            userInfo.setRole(roleValue);
            ProfileStater.startWithUserInfo(context, userInfo, tab);
        }
        callback.onInterrupt(null);

        return false;
    }
}
