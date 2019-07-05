package com.oneone.modules.mystory.contract;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.mystory.bean.StoryImg;
import com.oneone.modules.user.bean.UserProfileUpdateBean;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface StoryContract {
    interface Model {
    }

    interface View extends IBaseView {

    }

    interface Presenter {

        void updateUserInfo(OnUserInfoUpdateListener onUserInfoUpdateListener, UserProfileUpdateBean updateBean);

        void getTags(int type, OnTagsGetListener listener);

        void updateStory(String json, OnEditStoryListener editStoryListener);

        StoryImg findStoryByGroupIndex(MyStoryPreviewBean storyPreviewBean, int groupIndex);

        int findMaxGroupIndex(MyStoryPreviewBean storyPreviewBean);
    }

    interface OnTagsGetListener {
        void onTagsGet(List<String> tags);
    }

    interface OnEditStoryListener {
        void onStoryUpdate(MyStoryPreviewBean storyPreviewBean);
    }

    interface OnUserInfoUpdateListener {
        void onUserInfoUpdate();
    }
}
