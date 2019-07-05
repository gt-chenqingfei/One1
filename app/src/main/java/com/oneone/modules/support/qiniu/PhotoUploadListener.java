package com.oneone.modules.support.qiniu;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/6/7.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface PhotoUploadListener {
    void onUploadCompleted(UploadParam param, List<UploadParam> group, boolean isAllEnd);

    void onUploadError(UploadParam param, Throwable e);

    void onUploadProgress(UploadParam param, double percent);

    void onUploadStart(UploadParam param);
}
