package com.oneone.modules.profile.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLog;
import android.view.View;

import com.oneone.R;
import com.oneone.event.EventProfileStoryUpdate;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.MeasureViewPager;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.mystory.bean.MyStoryPreviewBean;
import com.oneone.modules.profile.contract.ProfileStoryContract;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.profile.presenter.ProfileStoryPresenter;
import com.oneone.modules.profile.view.ProfileStoryMatcherSaidView;
import com.oneone.modules.profile.view.ProfileStorySummaryView;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.view.StoryView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@SuppressLint("ValidFragment")
@LayoutResource(R.layout.frag_profile_my_story)
public class ProfileStoryFragment extends AbstractProfileFragment<ProfileStoryPresenter, ProfileStoryContract.View> implements ProfileStoryContract.View {
    @BindView(R.id.frag_profile_my_story_summary_view)
    ProfileStorySummaryView mSummaryView;

    @BindView(R.id.frag_profile_my_story_story_view)
    StoryView mStoryView;

    @BindView(R.id.frag_profile_my_story_matcher_view)
    ProfileStoryMatcherSaidView mMatcherView;

    private UserInfo mUserInfo;
    private ProfilePresenter profilePresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        if (mUserInfo != null) {
            mPresenter.getMatcherSaid(mUserInfo.getUserId());
            mPresenter.getStoryInfo(mUserInfo.getUserId());
            bindView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public ProfileStoryFragment(MeasureViewPager pager, int position) {
        super(pager, position);
    }

    @Override
    public void bindUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        if (mMatcherView == null) {
            return;
        }
        bindView();
    }

    @Override
    public void bindProfilePresenter(ProfilePresenter presenter) {
        this.profilePresenter = presenter;
    }

    @Override
    public void onPullRefresh() {
        mPresenter.getMatcherSaid(mUserInfo.getUserId());
        mPresenter.getStoryInfo(mUserInfo.getUserId());
    }

    private void bindView() {
        mMatcherView.bindUserInfo(mUserInfo);
        mSummaryView.bindUserInfo(mUserInfo);
        mStoryView.bindUserInfo(mUserInfo);
        mMatcherView.bindProfilePresenter(profilePresenter);
    }

    @Override
    public ProfileStoryPresenter onPresenterCreate() {
        return new ProfileStoryPresenter();
    }

    @Override
    public void onGetMatcherSaid(List<MatcherInfo> infoList) {
        mMatcherView.notifyDataSetChange(infoList);
    }

    @Override
    public void onGetStoryInfo(MyStoryPreviewBean storyPreviewBean) {
        mStoryView.bindData(storyPreviewBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventStoryUpdate(EventProfileStoryUpdate event) {
        mPresenter.getStoryInfo(mUserInfo.getUserId());
    }
}
