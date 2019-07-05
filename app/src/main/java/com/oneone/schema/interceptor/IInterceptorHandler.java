package com.oneone.schema.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;

/**
 * @author qingfei.chen
 * @since 2018/7/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface IInterceptorHandler {
    boolean process(Context context,Postcard postcard, InterceptorCallback callback);
}
