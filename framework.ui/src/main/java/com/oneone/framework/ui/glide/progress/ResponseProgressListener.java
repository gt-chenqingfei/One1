package com.oneone.framework.ui.glide.progress;

import okhttp3.HttpUrl;

/**
 * @since 17/12/20.
 * Copyright © 2017 Here Technology Co.,Ltd. All rights reserved.
 */

public interface ResponseProgressListener {
    void update(HttpUrl url, long bytesRead, long contentLength);
}
