package com.oneone.support.share;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public interface Callback {
    void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap);

    void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable);

    void onCancel(ShareParams shareParams, Platform platform, int i);
}
