package com.oneone.modules.qa.presenter;

import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.event.EventQaAnswer;
import com.oneone.event.EventRefreshQaClassify;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.beans.QuestionData;
import com.oneone.modules.qa.contract.QaContract;
import com.oneone.modules.qa.dto.QaAnswersFortargetDto;
import com.oneone.modules.qa.dto.QaMatchValueDto;
import com.oneone.modules.qa.dto.QuestionListDto;
import com.oneone.modules.qa.model.QaModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.QaAnswer;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by here on 18/4/18.
 */

public class QaPresenter extends BasePresenter<QaContract.View> implements QaContract.Presenter {
    private QaModel qaModel;

    @Override
    public void onAttachView(QaContract.View view) {
        super.onAttachView(view);

        qaModel = new QaModel(view.getActivityContext());
    }

    @Override
    public void qaAnswered() {
        enqueue(new AsyncTask<Object, Void, ApiResult<QaAnswer>>() {

            @Override
            protected ApiResult<QaAnswer> doInBackground(Object... voids) {
                return qaModel.qaAnswered();
            }

            @Override
            protected void onPostExecute(ApiResult<QaAnswer> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    HereUser.getInstance().getUserInfo().setQaAnswer(result.getData());
                    EventBus.getDefault().post(new EventQaAnswer());
                }
            }
        });
    }

    @Override
    public void qaCountInfo() {
        enqueue(new AsyncTask<Object, Void, ApiResult<List<QuestionClassify>>>() {

            @Override
            protected ApiResult<List<QuestionClassify>> doInBackground(Object... voids) {
                return qaModel.qaCountInfo();
            }

            @Override
            protected void onPostExecute(ApiResult<List<QuestionClassify>> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().qaCountInfo(result.getData());

                    QaDataManager.getInstance(getView().getActivityContext()).questionClassifies = result.getData();
                    EventBus.getDefault().post(new EventRefreshQaClassify());
                }
            }
        });
    }

    @Override
    public void qaUnAnswerMust() {
        enqueue(new AsyncTask<Object, Void, ApiResult<QuestionListDto>>() {

            @Override
            protected ApiResult<QuestionListDto> doInBackground(Object... voids) {
                return qaModel.qaUnAnswerMust();
            }

            @Override
            protected void onPostExecute(ApiResult<QuestionListDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().qaUnAnswerMust(result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }

    @Override
    public void qaUnAnswerClassify(final String classifyId) {
        enqueue(new AsyncTask<Object, Void, ApiResult<List<QuestionData>>>() {

            @Override
            protected ApiResult<List<QuestionData>> doInBackground(Object... voids) {
                return qaModel.qaUnAnswerClassify(classifyId);
            }

            @Override
            protected void onPostExecute(ApiResult<List<QuestionData>> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().qaUnAnswerClassify(result.getData());
                }
            }
        });
    }

    @Override
    public void qaUnAnswerAll() {
        enqueue(new AsyncTask<Object, Void, ApiResult<QuestionListDto>>() {

            @Override
            protected ApiResult<QuestionListDto> doInBackground(Object... voids) {
                return qaModel.qaUnAnswerAll();
            }

            @Override
            protected void onPostExecute(ApiResult<QuestionListDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().qaUnAnswerAll(result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }

    @Override
    public void qaAnswered(final boolean isLoadMore, final int skip, final int pageCount, final String classifyId) {
        enqueue(new AsyncTask<Object, Void, ApiResult<QuestionListDto>>() {

            @Override
            protected ApiResult<QuestionListDto> doInBackground(Object... voids) {
                return qaModel.qaAnswered(skip, pageCount, classifyId);
            }

            @Override
            protected void onPostExecute(ApiResult<QuestionListDto> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.getData() == null) {
                        getView().qaAnswered(isLoadMore, 0, null);
                    } else {
                        getView().qaAnswered(isLoadMore, result.getData().getCount(), result.getData().getList());
                    }
                }
            }
        });
    }

    @Override
    public void qaAnswer(final String questionId, final String answerId) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                ApiResult result = qaModel.qaAnswer(questionId, answerId);
                return result;
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (result != null && result.getStatus() == 0) {
                    getView().qaAnswer(questionId);
                } else {
                    getView().showError(result.getMessage());
                }
            }
        });
    }

    @Override
    public void qaMatchValue(final String targetUserId) {
        enqueue(new AsyncTask<Object, Void, ApiResult<QaMatchValueDto>>() {

            @Override
            protected ApiResult<QaMatchValueDto> doInBackground(Object... voids) {
                return qaModel.qaMatchValue(targetUserId);
            }

            @Override
            protected void onPostExecute(ApiResult<QaMatchValueDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().qaMatchValue(result.getData().getMatchForClassify(), result.getData().getMatchForAll());
                }
            }
        });
    }

    @Override
    public void qaAnswersFortarget(final String targetUserId, final int skip, final int pageCount) {
        enqueue(new AsyncTask<Object, Void, ApiResult<QaAnswersFortargetDto>>() {

            @Override
            protected ApiResult<QaAnswersFortargetDto> doInBackground(Object... voids) {
                return qaModel.qaAnswersFortarget(targetUserId, skip, pageCount);
            }

            @Override
            protected void onPostExecute(ApiResult<QaAnswersFortargetDto> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().qaAnswersFortarget(result.getData().getCount(), result.getData().getList());
                }
            }
        });
    }
}
