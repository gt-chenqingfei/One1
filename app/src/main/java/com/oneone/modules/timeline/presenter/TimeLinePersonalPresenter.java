package com.oneone.modules.timeline.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/6/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLinePersonalPresenter extends TimeLineBasePresenter implements TimeLineContract.PersonalTimeLinePresenter {
    @Override
    public void getPersonalTimeLine(final String userId, final long lastTimelineId,
                                    final TimeLineContract.OnGetPersonalTimeLineListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<TimeLineContainerDTO>>() {
            @Override
            protected ApiResult<TimeLineContainerDTO> doInBackground(Object[] objects) {
                return manager.personalTimeLine(userId, lastTimelineId, pageCount);
            }

            @Override
            protected void onPostExecute(ApiResult<TimeLineContainerDTO> result) {
                super.onPostExecute(result);
                if (listener == null) {
                    return;
                }
                if (result == null) {
                    listener.onGetPersonalTimeLine(null, lastTimelineId != 0, true);
                    return;
                }

                if (result.getStatus() == ApiStatus.OK) {
                    List<TimeLine> timeLines = result.getData().getList();
                    boolean isLoadEnd = true;
                    if (timeLines != null && !timeLines.isEmpty()) {
                        isLoadEnd = timeLines.size() - pageCount < 0;
                    }
                    listener.onGetPersonalTimeLine(timeLines, lastTimelineId != 0, isLoadEnd);
                    return;
                }
                listener.onGetPersonalTimeLine(null, lastTimelineId != 0, true);
            }
        };
        enqueue(task);
    }
}
