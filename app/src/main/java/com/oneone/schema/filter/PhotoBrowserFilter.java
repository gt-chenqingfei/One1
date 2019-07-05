package com.oneone.schema.filter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.oneone.Constants;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;
import com.oneone.modules.support.activity.PhotoBrowserActivity;
import com.oneone.schema.interceptor.IInterceptorHandler;
import com.oneone.utils.ImageHelper;

import java.net.URLDecoder;

/**
 * @author qingfei.chen
 * @since 2018/7/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class PhotoBrowserFilter extends BaseFilter  {
    @Override
    public boolean process(Context context, Postcard postcard, InterceptorCallback callback) {
        if(super.process(context,postcard,callback)){
            return true;
        }
        String photoUrl = postcard.getUri().getQueryParameter("photo_url");
        if (TextUtils.isEmpty(photoUrl)) {
            callback.onInterrupt(null);
            return true;
        }

        photoUrl = URLDecoder.decode(photoUrl);
        if (!ImageHelper.isFullUrl(photoUrl)) {
            photoUrl = Constants.URL.QINIU_BASE_URL() + photoUrl;
        }

        PhotoBrowserActivity.launch(context, photoUrl);
        callback.onInterrupt(null);

        return false;
    }
}
