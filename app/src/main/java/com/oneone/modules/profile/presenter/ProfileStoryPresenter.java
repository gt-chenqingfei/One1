package com.oneone.modules.profile.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.matcher.relations.dto.MyMatchersDto;
import com.oneone.modules.matcher.relations.model.SinglesModel;
import com.oneone.modules.mystory.StoryModel;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.profile.contract.ProfileStoryContract;
import com.oneone.restful.ApiResult;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class ProfileStoryPresenter extends BasePresenter<ProfileStoryContract.View> implements ProfileStoryContract.Presenter {
    private SinglesModel mSinglesModel;
    private StoryModel mStoryModel;

    @Override
    public void onAttachView(ProfileStoryContract.View view) {
        super.onAttachView(view);
        mSinglesModel = new SinglesModel(view.getActivityContext());
        mStoryModel = new StoryModel(view.getActivityContext());
    }

    @Override
    public void getMatcherSaid(final String userId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<MyMatchersDto>>() {

            @Override
            protected ApiResult<MyMatchersDto> doInBackground(Object... objects) {
                return mSinglesModel.getMatcherSaid(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<MyMatchersDto> result) {
                super.onPostExecute(result);
                if (result.getStatus() != ApiStatus.OK) {
                    showError(result.getMessage());
                    return;
                }
                if (result.getData() == null) {
                    return;
                }
                MyMatchersDto dto = result.getData();
                if (dto == null) {
                    return;
                }

                getView().onGetMatcherSaid(dto.getList());
            }
        };

        enqueue(task);
    }

    @Override
    public void getStoryInfo(final String userId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<MyStoryPreviewBean>>() {

            @Override
            protected ApiResult<MyStoryPreviewBean> doInBackground(Object... objects) {
                return mStoryModel.getStory(userId);
            }

            @Override
            protected void onPostExecute(ApiResult<MyStoryPreviewBean> result) {
                super.onPostExecute(result);
                if(result == null){
                    return;
                }
                if (result.getStatus() != ApiStatus.OK) {
                    showError(result.getMessage());
                    return;
                }
                if (result.getData() == null) {
                    return;
                }
                MyStoryPreviewBean dto = result.getData();
                if (dto == null) {
                    return;
                }

                getView().onGetStoryInfo(dto);
            }
        };

        enqueue(task);
    }
}
