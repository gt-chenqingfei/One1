package com.oneone.modules.timeline.contract;

import com.oneone.api.request.TimelinePostDto;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.imagepicker.bean.ImageItem;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineImage;
import com.oneone.modules.timeline.bean.TimeLineTopic;
import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.modules.timeline.dto.TimeLineTopicSearchDTO;
import com.oneone.restful.ApiResult;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface TimeLineContract {
    interface Model {
        ApiResult newTimeLine(String jsonBody);

        ApiResult<TimeLineContainerDTO> homeTimeLine(long lastTimelineId, int pageCount);


        ApiResult<TimeLineContainerDTO> personalTimeLine(String userId, long lastTimelineId, int pageCount);

        ApiResult<TimeLineContainerDTO> byTopic(String topic, long lastTimelineId, int pageCount);

        ApiResult<TimeLineTopicSearchDTO> officialTopics(String topic);

        ApiResult delete(long timelineId);

        ApiResult report(long timelineId, int reportReason);

        ApiResult like(long timelineId, int likeType);

    }

    interface View extends IBaseView {
    }

    interface Presenter {
        void like(TimeLine timeLine, int likeType, int position);

        void delete(TimeLine timeLine, int position);
    }

    interface HomeTimeLinePresenter extends Presenter {
        void getHomeTimeLine(long lastTimelineId, OnGetHomeTimeLineListener listener);
    }

    interface TopicTimeLinePresenter extends Presenter {
        void getTopic(String topic, long lastTimelineId, OnGetTopicListener listener);
    }

    interface PersonalTimeLinePresenter extends Presenter {
        void getPersonalTimeLine(String userId, long lastTimelineId, OnGetPersonalTimeLineListener listener);
    }

    interface TopicSearchPresenter {
        void getTopicSearchResult(String topic, OnGetTopicSearchResultListener listener);
    }


    interface ReportPresenter {
        void report(long timelineId, int reportReason, OnReportListener reportListener);
    }

    interface INewTimeLineManager {
        void newTimeLine(String content, List<TimeLineImage> timeLineImages);

        void reSendTimeLine(TimeLine timeLine);

        void clear();

        List<TimeLine> getTimeLineSendWaitList();

        List<TimeLine> getTimeLineUnSendList();

        TimeLine getTimeLine4ImageText();

        TimeLine getTimeLine4Text();

        boolean isSendingTimeLine4ImageText();

        boolean isSendingTimeLine4Text();
    }

    interface OnGetHomeTimeLineListener {
        void onGetHomeTimeLine(List<TimeLine> timeLines, boolean isLoadMore, boolean isLoadEnd);
    }

    interface OnGetPersonalTimeLineListener {
        void onGetPersonalTimeLine(List<TimeLine> timeLines, boolean isLoadMore, boolean isLoadEnd);
    }

    interface OnGetTopicListener {
        void onGetTopicListener(TimeLineContainerDTO timeLineContainerDTOS, boolean isLoadMore, boolean isLoadEnd);
    }

    interface OnGetTopicSearchResultListener {
        void onGetTopicSearchResultListener(List<TimeLineTopic> timeLineTopic);
    }


    interface OnReportListener {
        void onReport(boolean isOk);
    }
}
