package com.oneone.modules.feedback.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.feedback.beans.Feedback;
import com.oneone.modules.feedback.beans.FeedbackListItem;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * Created by here on 18/6/8.
 */

public interface FeedbackContract {
    interface View extends IBaseView {
        void onFeedbackSend(ApiResult result);
        void onFeedbackList(int count, List<FeedbackListItem> feedbackList);
    }

    interface Presenter {
        void feedbackSend(String feedbackJsonStr);
        void feedbackList(Long maxTimestamp, int limit);
    }
}
