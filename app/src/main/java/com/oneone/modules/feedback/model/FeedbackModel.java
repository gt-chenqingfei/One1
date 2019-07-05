package com.oneone.modules.feedback.model;

import android.content.Context;

import com.oneone.api.FeedbackStub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.feedback.dto.FeedbackListDto;
import com.oneone.restful.ApiResult;

/**
 * Created by here on 18/6/8.
 */

public class FeedbackModel extends BaseModel {
    private FeedbackStub feedbackStub;

    public FeedbackModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.feedbackStub = factory.create(FeedbackStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult feedbackSend(String feedbackJsonStr) {
        ApiResult result = null;
        try {
            result = this.feedbackStub.feedbackSend(feedbackJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<FeedbackListDto> feedbackList(Long maxTimestamp, int limit) {
        ApiResult<FeedbackListDto> result = null;
        try {
            result = this.feedbackStub.feedbackList(maxTimestamp, limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
