package com.oneone.modules.timeline.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.dto.TimeLineTopicSearchDTO;
import com.oneone.modules.timeline.model.TimeLineModel;
import com.oneone.restful.ApiResult;

/**
 * @author qingfei.chen
 * @since 2018/6/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimelineReportPresenter extends BasePresenter<TimeLineContract.View> implements TimeLineContract.ReportPresenter {

    protected TimeLineModel manager;

    @Override
    public void onAttachView(TimeLineContract.View view) {
        super.onAttachView(view);
        manager = new TimeLineModel(getView().getActivityContext());
    }


    @Override
    public void report(final long timelineId, final int reportReason, final TimeLineContract.OnReportListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... objects) {
                return manager.report(timelineId, reportReason);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (result.getStatus() != ApiStatus.OK) {
                    getView().showError(result.getMessage());
                }
                listener.onReport(result.getStatus() == ApiStatus.OK);
            }
        };
        enqueue(task);
    }
}
