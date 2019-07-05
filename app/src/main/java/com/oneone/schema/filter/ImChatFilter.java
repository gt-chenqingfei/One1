package com.oneone.schema.filter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.activity.ImTalkActivity;
import com.oneone.modules.msg.beans.IMUserPrerelation;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.schema.interceptor.IInterceptorHandler;

/**
 * @author qingfei.chen
 * @since 2018/7/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ImChatFilter extends BaseFilter {
    @Override
    public boolean process(final Context context, Postcard postcard, InterceptorCallback callback) {
        if(super.process(context,postcard,callback)){
            return true;
        }
        final String targetId = postcard.getUri().getQueryParameter("targetUserId");

        IMManager.getInstance().startConversationWithCallBack(context, targetId, new IMManager.ConversationListener() {
            @Override
            public void onUserRelation(IMUserPrerelation imUserPrerelation) {
                UserInfo info =new UserInfo();
                info.setUserId(targetId);
                ImTalkActivity.startActivity(context, imUserPrerelation, info);
            }
        });
        callback.onInterrupt(null);

        return false;
    }
}
