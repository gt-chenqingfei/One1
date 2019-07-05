package com.oneone.modules.feedback.presenter;

import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.modules.feedback.contract.FeedbackContract;
import com.oneone.modules.feedback.dto.FeedbackListDto;
import com.oneone.modules.feedback.model.FeedbackModel;
import com.oneone.restful.ApiResult;

/**
 * Created by here on 18/6/8.
 */

public class FeedbackPresenter extends BasePresenter<FeedbackContract.View> implements FeedbackContract.Presenter {
    private FeedbackModel feedbackModel;

    @Override
    public void onAttachView(FeedbackContract.View view) {
        super.onAttachView(view);

        feedbackModel = new FeedbackModel(view.getActivityContext());
    }

    @Override
    public void feedbackSend(final String feedbackJsonStr) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {
            @Override
            protected ApiResult doInBackground(Object... voids) {
                return feedbackModel.feedbackSend(feedbackJsonStr);
            }
            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().onFeedbackSend(result);
            }
        });
    }

    @Override
    public void feedbackList(final Long maxTimestamp, final int limit) {
        enqueue(new AsyncTask<Object, Void, ApiResult<FeedbackListDto>>() {
            @Override
            protected ApiResult<FeedbackListDto> doInBackground(Object... voids) {
                return feedbackModel.feedbackList(maxTimestamp, limit);
            }
            @Override
            protected void onPostExecute(ApiResult<FeedbackListDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onFeedbackList(result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }
}
