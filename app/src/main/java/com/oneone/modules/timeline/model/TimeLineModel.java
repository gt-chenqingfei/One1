package com.oneone.modules.timeline.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.entry.beans.AccountInfo;
import com.oneone.api.AccountStub;
import com.oneone.api.TimeLineStub;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.modules.timeline.dto.TimeLineTopicSearchDTO;
import com.oneone.restful.ApiResult;
import com.oneone.restful.InvocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineModel extends BaseModel implements TimeLineContract.Model {
    private TimeLineStub mTimeLineStub;
    Logger logger = LoggerFactory.getLogger(TimeLineModel.class.getSimpleName());

    public TimeLineModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.mTimeLineStub = factory.create(TimeLineStub.class, RestfulAPI.BASE_API_URL,
                RestfulAPI.getParams(context));
    }

    @Override
    public ApiResult newTimeLine(String jsonBody) {
        return this.mTimeLineStub.newTimeLine(jsonBody);
    }

    @Override
    public ApiResult<TimeLineContainerDTO> homeTimeLine(long lastTimelineId, int pageCount) {
        try {
            return mTimeLineStub.homeTimeLine(lastTimelineId, pageCount);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResult<TimeLineContainerDTO> personalTimeLine(String userId, long lastTimelineId, int pageCount) {
        try {
            return mTimeLineStub.personalTimeLine(userId, lastTimelineId, pageCount);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResult<TimeLineContainerDTO> byTopic(String topic, long lastTimelineId, int pageCount) {
        try {
            return mTimeLineStub.bytopic(topic, lastTimelineId, pageCount);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResult<TimeLineTopicSearchDTO> officialTopics(String topic) {
        try {
            return mTimeLineStub.officialtopics(topic);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResult delete(long timelineId) {

        try {
            return mTimeLineStub.delete(timelineId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResult report(long timelineId, int reportReason) {
        try {
            return mTimeLineStub.report(timelineId, reportReason);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public ApiResult like(long timelineId, int likeType) {
        try {
            return mTimeLineStub.like(timelineId, likeType);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
