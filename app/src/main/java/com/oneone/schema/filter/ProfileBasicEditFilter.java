package com.oneone.schema.filter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.oneone.api.constants.Role;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.activity.ModifyMatcherUserBasicActivity;
import com.oneone.modules.user.activity.ModifySingleUserBasicActivity;
import com.oneone.schema.interceptor.IInterceptorHandler;

/**
 * @author qingfei.chen
 * @since 2018/7/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ProfileBasicEditFilter extends BaseFilter {
    @Override
    public boolean process(Context context, Postcard postcard, InterceptorCallback callback) {
        if(super.process(context,postcard,callback)){
            return true;
        }
        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
            ModifyMatcherUserBasicActivity.startActivity(context);
        } else {
            ModifySingleUserBasicActivity.startActivity(context);
        }
        callback.onInterrupt(null);

        return false;
    }
}
