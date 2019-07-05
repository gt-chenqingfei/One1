package com.oneone.modules.timeline.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.api.constants.ApiStatus;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/6/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineTopicPresenter extends TimeLineBasePresenter implements TimeLineContract.TopicTimeLinePresenter {
    @Override
    public void getTopic(final String topic, final long lastTimelineId, final TimeLineContract.OnGetTopicListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<TimeLineContainerDTO>>() {

            @Override
            protected ApiResult<TimeLineContainerDTO> doInBackground(Object... objects) {
                return manager.byTopic(topic, lastTimelineId, pageCount);
            }

            @Override
            protected void onPostExecute(ApiResult<TimeLineContainerDTO> timeLineContainerDTOApiResult) {
                super.onPostExecute(timeLineContainerDTOApiResult);
                if (listener == null) {
                    return;
                }
                if (timeLineContainerDTOApiResult == null) {
                    listener.onGetTopicListener(null, lastTimelineId != 0, true);
                    return;
                }

                if (timeLineContainerDTOApiResult.getStatus() == ApiStatus.OK) {
                    TimeLineContainerDTO timeLineContainerDTO = timeLineContainerDTOApiResult.getData();
                    boolean isLoadEnd = true;
                    if (timeLineContainerDTO != null && !timeLineContainerDTO.getList().isEmpty()) {
                        isLoadEnd = timeLineContainerDTO.getList().size() - pageCount < 0;
                    }
                    listener.onGetTopicListener(timeLineContainerDTO, lastTimelineId != 0, isLoadEnd);
                    return;
                }
                listener.onGetTopicListener(null, lastTimelineId != 0, true);
            }
        };
        enqueue(task);
    }

}
