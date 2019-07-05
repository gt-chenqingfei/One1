package com.oneone.schema.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * @author qingfei.chen
 * @version 1.0
 * @since 2018-07-11 11:48:24
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Interceptor(priority = 7)
public class SchemaInterceptor implements IInterceptor {
    Context mContext;

    /**
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */
    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        InterceptorHandler.process(mContext, postcard, callback);
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {
        mContext = context;
        InterceptorHandler.register();
    }
}
