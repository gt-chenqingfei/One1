package com.oneone.modules.support.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.constants.ApiStatus;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.framework.ui.BaseModel;
import com.oneone.framework.ui.imagepicker.photoview.log.Logger;
import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.modules.entry.beans.UploadTokenBean;
import com.oneone.api.SupportStub;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.restful.ApiResult;
import com.oneone.utils.AssetsUtil;

import org.slf4j.LoggerFactory;

import java.util.List;

public class SupportModel extends BaseModel {
    private SupportStub mSupportStub = null;
    private Context mContext;

    public SupportModel(Context context) {
        super(context);
        this.mContext = context;
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.mSupportStub = factory.create(SupportStub.class,
                RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public List<Occupation> getOccupationList() {
        List<Occupation> list = null;
        try {
            final ApiResult<List<Occupation>> result = this.mSupportStub.occupationList();
            if (null == result) {
                return null;
            }
            list = result.getData();

            if (list != null) {
                Log.e("m", list.size() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<City> getCityList() {
        List<City> list = null;
        try {
            final ApiResult<List<City>> result = this.mSupportStub.citylist();
            if (null == result) {
                return null;
            }
            list = result.getData();

            if (list != null) {
                Log.e("m", list.size() + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ApiResult<UploadTokenBean> getUploadToken() {
        try {
            return this.mSupportStub.getUploadToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getUploadTokenBackground(final OnTokenGetListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<UploadTokenBean>>() {
            @Override
            protected ApiResult<UploadTokenBean> doInBackground(Object[] objects) {
                return getUploadToken();
            }

            @Override
            protected void onPostExecute(ApiResult<UploadTokenBean> result) {
                super.onPostExecute(result);
                if (listener == null) {
                    return;
                }
                if (result == null) {
                    return;
                }
                if (result.getStatus() != ApiStatus.OK) {
                    listener.onTokenGet(null);
                    return;
                }
                listener.onTokenGet(result.getData());
            }
        };
        task.execute();
    }

    public void getShareInfo() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {
            @Override
            protected ApiResult doInBackground(Object[] objects) {
                return mSupportStub.sharedInfo();
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (result == null || result.getStatus() != ApiStatus.OK) {
                    // 在 assets 中放置一份分享的 json，如果服务端返回空的时候使用
                    String content = AssetsUtil.getContentFromAssets(mContext, ShareInfo.SHARE_INFO_JSON);
                    DefaultSP.getInstance().put(mContext, ShareInfo.SHARE_KEY, content).apply();
                    return;
                }
                DefaultSP.getInstance().put(mContext, ShareInfo.SHARE_KEY, result.getData().toString()).apply();
            }
        };
        task.execute();
    }

    public void postDeviceInfo(final String token, final String channel) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {
            @Override
            protected ApiResult doInBackground(Object[] objects) {
                return mSupportStub.deviceInfo(token, channel);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (result == null) {
                    LoggerFactory.getLogger("SupportModel").error("postDeviceInfo error! ");
                    return;
                }
                if (result.getStatus() != ApiStatus.OK) {
                    LoggerFactory.getLogger("SupportModel").error("postDeviceInfo error! error:" + result.getMessage());
                }
            }
        };
        task.execute();
    }

    public interface OnTokenGetListener {
        void onTokenGet(UploadTokenBean tokenBean);
    }
}
