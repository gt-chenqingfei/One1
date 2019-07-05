package com.oneone.schema.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.oneone.schema.filter.ImChatFilter;
import com.oneone.schema.filter.PhotoBrowserFilter;
import com.oneone.schema.filter.ProfileBasicEditFilter;
import com.oneone.schema.filter.ProfileInfoFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qingfei.chen
 * @since 2018/7/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class InterceptorHandler {
    private static Map<String, IInterceptorHandler> interceptorMap = new HashMap<>();

    public static void process(Context context, Postcard postcard, InterceptorCallback interceptor) {
        IInterceptorHandler handler = interceptorMap.get(postcard.getPath());
        if (handler != null) {
            handler.process(context, postcard, interceptor);
        } else {
            interceptor.onContinue(postcard);
        }
    }

    public static void register() {
        interceptorMap.put("/profile/edit_basic", new ProfileBasicEditFilter());
        interceptorMap.put("/profile/info", new ProfileInfoFilter());
        interceptorMap.put("/im/chat", new ImChatFilter());
        interceptorMap.put("/photo/browser", new PhotoBrowserFilter());
    }
}
