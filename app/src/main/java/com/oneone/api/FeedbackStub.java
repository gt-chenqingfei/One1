package com.oneone.api;

import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.feedback.dto.FeedbackListDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

/**
 * Created by here on 18/6/8.
 */

public interface FeedbackStub extends ServiceStub {
    @HttpPost("/feedback/send")
    ApiResult feedbackSend(@BodyJsonParameter("feedbackInfoJsonStr") String feedbackInfoJsonStr);
    @HttpGet("/feedback/listmsg")
    ApiResult<FeedbackListDto> feedbackList(@QueryParameter("maxTimestamp") Long maxTimestamp, @QueryParameter("limit") int limit);
}
