package com.oneone.support.qiniu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.oneone.modules.entry.beans.UploadTokenBean;

public class UploadObj {
    public static final String UPLOAD_SUCCESS = "success";
    public static final String UPLOAD_FAILED = "failed";

    private String uploadType;
    private Uri uploadUri;
    private UploadTokenBean uploadTokenBean;
    private String status;

    private String serverPath;

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public Uri getUploadUri() {
        return uploadUri;
    }

    public void setUploadUri(Uri uploadUri) {
        this.uploadUri = uploadUri;
    }

    public UploadTokenBean getUploadTokenBean() {
        return uploadTokenBean;
    }

    public void setUploadTokenBean(UploadTokenBean uploadTokenBean) {
        this.uploadTokenBean = uploadTokenBean;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    @Override
    public String toString() {
        return "UploadObj{" +
                "uploadType='" + uploadType + '\'' +
                ", uploadUri=" + uploadUri +
                ", uploadTokenBean=" + uploadTokenBean +
                ", status='" + status + '\'' +
                '}';
    }
}
