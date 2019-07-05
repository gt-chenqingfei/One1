package com.oneone.modules.support.qiniu;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.oneone.HereSingletonFactory;
import com.oneone.framework.android.environment.StorageType;
import com.oneone.framework.android.environment.StorageUtil;
import com.oneone.modules.entry.beans.UploadTokenBean;
import com.oneone.modules.support.model.SupportModel;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * @author qingfei.chen
 * @since 2018/6/7.
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class PhotoUploadManager implements UpCompletionHandler, UpProgressHandler, UpCancellationSignal {
    private Logger logger = LoggerFactory.getLogger("PhotoUploadManager");
    private UploadManager mUploadManager;
    private volatile boolean isCancelled = false;
    private Map<String, UploadParam> mUploadQueue;
    private SparseArray<List<UploadParam>> mUploadGroupQueue;
    private Context mContext;

    private PhotoUploadManager(Context context) {
        this.mContext = context;
        mUploadManager = new UploadManager();
        mUploadQueue = new HashMap<>();
        mUploadGroupQueue = new SparseArray<>();
    }

    private void compressAndUpload(final UploadParam param) {
        Luban.with(mContext)
                .load(param.filePath)
                .ignoreBy(500)
                .setTargetDir(StorageUtil.getDirectoryByDirType(StorageType.TYPE_IMAGE))
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        param.listener.onUploadStart(param);
                    }

                    @Override
                    public void onSuccess(File file) {
                        param.setCompressPath(file.getPath());
                        uploadAfterTokenGet(param);
                    }

                    @Override
                    public void onError(Throwable e) {
                        param.listener.onUploadError(param, e);
                    }
                }).launch();    //Start compress
    }

    private void uploadAfterTokenGet(final UploadParam param) {

        if (param == null) {
            logger.error("UploadParam argument must be not null!");
            return;
        }
        if (TextUtils.isEmpty(param.filePath)) {
            logger.error("UploadParam argument filePath field is required !!");
            return;
        }

        new SupportModel(mContext).getUploadTokenBackground(new SupportModel.OnTokenGetListener() {
            @Override
            public void onTokenGet(UploadTokenBean tokenBean) {
                if (tokenBean == null) {
                    logger.error("Get token error !!");
                    param.listener.onUploadError(param, new IllegalArgumentException());
                    return;
                }

                param.setKey(tokenBean.getPath());
                param.setRemotePath(tokenBean.getPath());
                param.setToken(tokenBean.getToken());
                upload(param);
            }
        });
    }

    /**
     * Upload single image and compress
     *
     * @param param Upload file param
     */
    public void enqueue(UploadParam param) {
        if (param == null) {
            logger.error("UploadParam param must be not null!");
            return;
        }
        List<UploadParam> list = new ArrayList<>();
        list.add(param);
        mUploadGroupQueue.put(param.groupId, list);
        compressAndUpload(param);
    }

    /**
     * Upload single images and compress
     *
     * @param params Upload files param
     */
    public void enqueueWithGroup(List<UploadParam> params) {
        if (params == null || params.isEmpty()) {
            logger.error("UploadParam list must be not null!");
            return;
        }
        UploadParam first = params.get(0);
        mUploadGroupQueue.put(first.groupId, params);

        for (UploadParam param : params) {
            compressAndUpload(param);
        }
    }

    private void upload(UploadParam param) {
        logger.info("To upload:" + param.toString());
        mUploadQueue.put(param.getKey(), param);
        mUploadManager.put(param.filePath, param.getKey(), param.getToken(), this,
                new UploadOptions(null, null, false,
                        this, this));
    }

    /**
     * Cancel all upload handle
     */
    public void cancel() {
        isCancelled = true;
    }

    /**
     * Upload complete
     *
     * @param key      key
     * @param info     info
     * @param response response
     */
    @Override
    public void complete(String key, ResponseInfo info, JSONObject response) {
        UploadParam param = mUploadQueue.get(key);
        if (param == null) {
            return;
        }

        if (!info.isOK()) {
            param.listener.onUploadError(param, new IllegalArgumentException());
            mUploadQueue.remove(key);
            mUploadGroupQueue.remove(param.groupId);
            return;
        }

        param.setCompleted(true);
        List<UploadParam> group = mUploadGroupQueue.get(param.groupId);
        int completeCount = 0;
        for (UploadParam item : group) {
            if (item.isCompleted()) {
                completeCount++;
            }
        }
        boolean isAllEnd = completeCount == group.size();
        if (isAllEnd) {
            mUploadQueue.remove(key);
            mUploadGroupQueue.remove(param.groupId);
        }

        if (param.listener != null) {
            param.listener.onUploadCompleted(param, group, isAllEnd);
        }

    }

    /**
     * Upload progress
     *
     * @param key     upload key
     * @param percent upload percent
     */
    @Override
    public void progress(String key, double percent) {
        UploadParam param = mUploadQueue.get(key);
        if (param == null || param.listener == null) {
            return;
        }

        param.listener.onUploadProgress(param, percent);
    }

    /**
     * Upload cancel
     *
     * @return isCanceled
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
}
