package com.oneone.api;


import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.modules.timeline.dto.TimeLineTopicSearchDTO;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;


/**
 * create by qingfei.chen
 */
public interface TimeLineStub extends ServiceStub {
    @HttpPost("/timeline/new")
    ApiResult newTimeLine(@BodyJsonParameter("content") String jsonBody);

    @HttpPost("/timeline/like")
    ApiResult like(@BodyParameter("timelineId") long timelineId, @BodyParameter("likeType") int likeType);

    @HttpGet("/timeline/home")
    ApiResult<TimeLineContainerDTO> homeTimeLine(@QueryParameter("lastTimelineId") long lastTimelineId, @QueryParameter("pageCount") int pageCount);

    @HttpGet("/timeline/personal")
    ApiResult<TimeLineContainerDTO> personalTimeLine(@QueryParameter("userId") String userId, @QueryParameter("lastTimelineId") long lastTimelineId, @QueryParameter("pageCount") int pageCount);

    @HttpGet("/timeline/bytopic")
    ApiResult<TimeLineContainerDTO> bytopic(@QueryParameter("topic") String topic, @QueryParameter("lastTimelineId") long lastTimelineId, @QueryParameter("pageCount") int pageCount);

    @HttpGet("/timeline/officialtopics")
    ApiResult<TimeLineTopicSearchDTO> officialtopics(@QueryParameter("topic") String topic);

    @HttpPost("/timeline/delete")
    ApiResult delete(@BodyParameter("timelineId") long timelineId);

    @HttpPost("/timeline/report")
    ApiResult report(@BodyParameter("timelineId") long timelineId, @BodyParameter("reportReason") int reportReason);
}
