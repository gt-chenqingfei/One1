package com.oneone.schema.filter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.modules.SplashActivity;
import com.oneone.modules.entry.activity.LoginActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.schema.interceptor.IInterceptorHandler;

/**
 * @author qingfei.chen
 * @since 2018/7/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class BaseFilter implements IInterceptorHandler {
    @Override
    public boolean process(Context context, Postcard postcard, InterceptorCallback callback) {
        HereUser user = HereUser.getInstance();
        if (user == null || TextUtils.isEmpty(user.getToken())) {
            ARouter.getInstance().build("/entry/login").navigation(context);
            return true;
        }
        return false;
    }
}
