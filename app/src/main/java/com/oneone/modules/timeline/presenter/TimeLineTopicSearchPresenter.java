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
public class TimeLineTopicSearchPresenter extends BasePresenter<TimeLineContract.View> implements TimeLineContract.TopicSearchPresenter {

    protected TimeLineModel manager;

    @Override
    public void onAttachView(TimeLineContract.View view) {
        super.onAttachView(view);
        manager = new TimeLineModel(getView().getActivityContext());
    }

    @Override
    public void getTopicSearchResult(final String topic, final TimeLineContract.OnGetTopicSearchResultListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<TimeLineTopicSearchDTO>>() {

            @Override
            protected ApiResult<TimeLineTopicSearchDTO> doInBackground(Object... objects) {
                return manager.officialTopics(topic);
            }

            @Override
            protected void onPostExecute(ApiResult<TimeLineTopicSearchDTO> timeLineTopicSearchDTOApiResult) {
                super.onPostExecute(timeLineTopicSearchDTOApiResult);
                if (listener == null) {
                    return;
                }
                if (timeLineTopicSearchDTOApiResult == null) {
                    listener.onGetTopicSearchResultListener(null);
                    return;
                }
                if (timeLineTopicSearchDTOApiResult.getStatus() == ApiStatus.OK) {
                    TimeLineTopicSearchDTO timeLineTopicSearchDTO = timeLineTopicSearchDTOApiResult.getData();
                    listener.onGetTopicSearchResultListener(timeLineTopicSearchDTO.getList());
                    return;
                }
                listener.onGetTopicSearchResultListener(null);
            }
        };
        enqueue(task);
    }
}
