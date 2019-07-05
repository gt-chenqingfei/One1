package com.oneone.modules.mystory.presenter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.HereSingletonFactory;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.mystory.StoryModel;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.restful.ApiResult;

import java.util.List;


/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class StoryPresenter extends BasePresenter<StoryContract.View> implements
        StoryContract.Presenter {
    StoryModel manager;

    @Override
    public void onAttachView(StoryContract.View view) {
        super.onAttachView(view);
        manager = new StoryModel(view.getActivityContext());
    }

    @Override
    public void updateUserInfo(final StoryContract.OnUserInfoUpdateListener listener, UserProfileUpdateBean updateBean) {

        getView().loading("");
        HereSingletonFactory.getInstance().getUserManager().updateUserInfo(new UserManager.UserUpdateListener() {
            @Override
            public void onUserUpdate(UserInfo userInfo, boolean isOk, String message) {
                getView().loadingDismiss();
                if (!isOk) {
                    getView().showError(message);
                    return;
                }
                if (listener != null) {
                    listener.onUserInfoUpdate();
                }
            }
        }, updateBean);
    }

    @Override
    public void getTags(int type, final StoryContract.OnTagsGetListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Integer, Object, ApiResult<List<String>>>() {

            @Override
            protected ApiResult<List<String>> doInBackground(Integer... params) {
                return manager.getStoryTagsByType(params[0]);
            }

            @Override
            protected void onPostExecute(ApiResult<List<String>> result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }
                if (result.getStatus() != ApiStatus.OK) {
                    getView().showError(result.getMessage());
                    return;
                }

                if (result.getData() == null) {
                    return;
                }

                List<String> userStoryTags = result.getData();
                if (listener != null) {
                    listener.onTagsGet(userStoryTags);
                }
            }
        };
        enqueue(task, type);
    }

    @Override
    public void updateStory(final String storyJson, final StoryContract.OnEditStoryListener listener) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Object, ApiResult<MyStoryPreviewBean>>() {

            @Override
            protected ApiResult<MyStoryPreviewBean> doInBackground(Object... params) {
                return manager.uploadMyStoryImgs(storyJson);
            }

            @Override
            protected void onPostExecute(ApiResult<MyStoryPreviewBean> result) {
                super.onPostExecute(result);
                if (result == null) {
                    return;
                }
                if (result.getStatus() != ApiStatus.OK) {
                    getView().showError(result.getMessage());
                    return;
                }

                if (listener != null) {
                    listener.onStoryUpdate(result.getData());
                }
            }
        };
        enqueue(task);
    }

    @Override
    public StoryImg findStoryByGroupIndex(MyStoryPreviewBean storyPreviewBean, int groupIndex) {
        if (storyPreviewBean == null) {
            return null;
        }
        if (storyPreviewBean.getImgs() == null) {
            return null;
        }

        for (StoryImg img : storyPreviewBean.getImgs()) {
            if (img.getGroupIndex() == groupIndex) {
                return img;
            }
        }
        return null;
    }


    @Override
    public int findMaxGroupIndex(MyStoryPreviewBean storyPreviewBean) {
        int maxIndex = 0;
        if (storyPreviewBean != null) {
            for (StoryImg img : storyPreviewBean.getImgs()) {
                maxIndex = Math.max(maxIndex, img.getGroupIndex());
            }
        }
        return maxIndex;
    }

}
