package com.oneone.modules.find.presenter;

import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.contract.FindContract;
import com.oneone.modules.find.dto.FindPageUserInfoDTO;
import com.oneone.modules.find.dto.FindPageUserInfoListDto;
import com.oneone.modules.find.model.FindModel;
import com.oneone.modules.find.model.LikeModel;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.restful.ApiResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public class FindPresenter extends BasePresenter<FindContract.View> implements FindContract.Presenter {
    private FindModel findModel;
    private LikeModel likeModel;

    @Override
    public void onAttachView(FindContract.View view) {
        super.onAttachView(view);

        findModel = new FindModel(view.getActivityContext());
        likeModel = new LikeModel(view.getActivityContext());
    }

    @Override
    public void findRecommend() {
        enqueue(new AsyncTask<Object, Void, ApiResult<FindPageUserInfoListDto>>() {
            @Override
            protected ApiResult<FindPageUserInfoListDto> doInBackground(Object... voids) {
                return findModel.findRecommend();
            }

            @Override
            protected void onPostExecute(ApiResult<FindPageUserInfoListDto> result) {
                super.onPostExecute(result);
                RedDotManager.getInstance().notifyFindDotChanged(false);
                if (result != null && result.getData() != null) {
                    getView().onFindRecommend(result.getData().getUserList(), result.getData().getExpire(), result.getData().getRecommendSize());
                }
            }
        });
    }

    @Override
    public void findSetCondition(final String findConditionStr) {
        enqueue(new AsyncTask<Object, Void, ApiResult<FindPageUserInfoListDto>>() {

            @Override
            protected ApiResult<FindPageUserInfoListDto> doInBackground(Object... voids) {
                return findModel.findSetCondition(findConditionStr);
            }

            @Override
            protected void onPostExecute(ApiResult<FindPageUserInfoListDto> result) {
                super.onPostExecute(result);
                getView().onFindSetCondition();
            }
        });
    }


    @Override
    public void findGetCondition() {
        enqueue(new AsyncTask<Object, Void, ApiResult<FindCondition>>() {

            @Override
            protected ApiResult<FindCondition> doInBackground(Object... voids) {
                return findModel.findGetCondition();
            }

            @Override
            protected void onPostExecute(ApiResult<FindCondition> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onFindGetCondition(result.getData());
                } else {
                    getView().onFindGetCondition(null);
                }
            }
        });
    }

    public void likeSetLike(final String userId) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return likeModel.likeSetLike(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);

                getView().onSetLike();
            }
        });
    }

    public void likeCancelLike(final String userId) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return likeModel.likeCancelLike(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                getView().onCancelLike();
            }
        });
    }

    public void likeSetNoFeel(final String userId) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return likeModel.likeSetNoFeel(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().onSetNoFeel();
            }
        });
    }

    public void likeCancelNoFeel(final String userId) {
        enqueue(new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return likeModel.likeCancelNoFeel(userId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().onCancelNoFeel();
            }
        });
    }
}
