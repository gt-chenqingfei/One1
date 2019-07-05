package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.oneone.Constants;
import com.oneone.R;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.analytics.annotation.Alias;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.view.StoryView;
import com.oneone.utils.ImageHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Alias("预览效果")
@LayoutResource(R.layout.frag_profile_preview)
public class ProfilePreviewFrag extends BaseFragment {

    @BindView(R.id.preview_element_user_photo_iv)
    ImageView previewElementUserPhotoIv;

    @BindView(R.id.frag_profile_preview_my_story_view)
    StoryView mStoryView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPhotoParams();
        refreshView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshView();
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void refreshView() {
        String json = UserSP.getString(getContext(), Constants.PREF.PREF_STORY_PREVIEW_BEAN, "");
        if (!TextUtils.isEmpty(json)) {
            MyStoryPreviewBean bean1 = new Gson().fromJson(json, MyStoryPreviewBean.class);
            ImageHelper.displayAvatar(getContext(), previewElementUserPhotoIv, OpenRelationPresenter.getTempUserInfo().getAvatar());
            mStoryView.bindData(bean1);
            mStoryView.bindUserInfo(HereUser.getInstance().getUserInfo());
        }
    }

    public void initPhotoParams() {
        LinearLayout.LayoutParams photoParams = (LinearLayout.LayoutParams) previewElementUserPhotoIv.getLayoutParams();
        photoParams.width = ScreenUtil.getDisplayWidth();
        photoParams.height = ScreenUtil.getDisplayWidth();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextStepEvent(EventNextStep event) {

        EventBus.getDefault().post(new EventProfileUpdateByRole(null));
    }

}
