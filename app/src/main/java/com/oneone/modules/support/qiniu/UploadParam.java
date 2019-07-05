package com.oneone.modules.support.qiniu;

import com.oneone.Constants;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/6/7.
 */
public class UploadParam {
    public final String filePath;
    public final PhotoUploadListener listener;
    public final int groupId;
    private String compressPath;
    private String remotePath;
    private String key;
    private String token;
    private int width;
    private int height;
    private boolean isCompleted = false;

    public UploadParam(int groupId, String filePath, PhotoUploadListener listener) {
        this.filePath = filePath;
        this.listener = listener;
        this.groupId = groupId;
    }

    public UploadParam setKey(String key) {
        this.key = key;
        return this;
    }

    public UploadParam setToken(String token) {
        this.token = token;
        return this;
    }

    public UploadParam setRemotePath(String remotePath) {
        this.remotePath = remotePath;
        return this;
    }

    public UploadParam setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public UploadParam setCompressPath(String compressPath) {
        this.compressPath = compressPath;
        return this;
    }

    public String getRemoteUrl() {
        return Constants.URL.QINIU_BASE_URL() + remotePath;
    }

    public String getKey() {
        return key;
    }

    public String getToken() {
        return token;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
