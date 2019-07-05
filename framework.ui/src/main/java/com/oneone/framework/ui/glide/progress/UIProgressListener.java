package com.oneone.framework.ui.glide.progress;

/**
 * @since 17/12/20.
 * Copyright © 2017 Here Technology Co.,Ltd. All rights reserved.
 */

public interface UIProgressListener {
    void onProgress(long bytesRead, long expectedLength);

    float getGranularityPercentage();
}
