package com.oneone.modules.profile.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oneone.R;
import com.oneone.api.constants.TimelineStatus;
import com.oneone.event.EventTimelineDelete;
import com.oneone.event.EventTimelineLike;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.MeasureViewPager;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.profile.adapter.ProfileTimeLineAdapter;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.profile.view.EmptyView4Timeline;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.presenter.TimeLinePersonalPresenter;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.widget.NoMorelData4Common;

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
@LayoutResource(R.layout.frag_profile_timeline)
public class ProfileTimelineFragment extends AbstractProfileFragment<TimeLinePersonalPresenter, TimeLineContract.View>
        implements TimeLineContract.View, TimeLineContract.OnGetPersonalTimeLineListener {
    @BindView(R.id.layout_profile_timeline_recycler_view)
    SimplePullRecyclerView<MatcherInfo> simpleRecyclerView;

    private ProfileTimeLineAdapter mTimeLineAdapter;
    private UserInfo mUserInfo;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimeLineAdapter = new ProfileTimeLineAdapter(null, getContext(), mPresenter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        simpleRecyclerView.setAdapter(mTimeLineAdapter, layoutManager);
        simpleRecyclerView.setEmptyView(new EmptyView4Timeline(getContext()));
        simpleRecyclerView.setRefreshEnable(false);
        mPresenter.getPersonalTimeLine(mUserInfo.getUserId(), 0, this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public ProfileTimelineFragment(MeasureViewPager pager, int position) {
        super(pager, position);
    }

    @Override
    public void bindUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    @Override
    public void bindProfilePresenter(ProfilePresenter presenter) {

    }

    @Override
    public void onPullRefresh() {
        mPresenter.getPersonalTimeLine(mUserInfo.getUserId(), 0, this);
    }

    @Override
    public TimeLinePersonalPresenter onPresenterCreate() {
        return new TimeLinePersonalPresenter();
    }

    @Override
    public void onLoadMore() {
        if (simpleRecyclerView.isNoMoreData()) {
            return;
        }
        if (mPresenter == null) {
            return;
        }

        if (mTimeLineAdapter.getItemCount() <= 0) {
            return;
        }

        TimeLine line = mTimeLineAdapter.getItem(mTimeLineAdapter.getItemCount() - 1);
        if (null != line) {
            mPresenter.getPersonalTimeLine(mUserInfo.getUserId(), line.getTimelineId(), this);
        }
    }

    @Override
    public void onGetPersonalTimeLine(List<TimeLine> timeLines, boolean isLoadMore, boolean isLoadEnd) {
        if (null == timeLines || timeLines.isEmpty()) {
            return;
        }

        simpleRecyclerView.setNoMoreData(isLoadEnd);
        if (isLoadMore) {
            mTimeLineAdapter.addData(timeLines);
        } else {
            mTimeLineAdapter.notifyDataChange(timeLines);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLineLikeEvent(EventTimelineLike event) {
        TimeLine timeLine = mTimeLineAdapter.findItemById(event.getTimeLine().getTimelineId());
        if (timeLine == null) {
            return;
        }
        int position = mTimeLineAdapter.index(timeLine);
        if (position != -1) {
            mTimeLineAdapter.notifyItemChanged(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimegLineDeleteEvent(EventTimelineDelete event) {
        TimeLine timeLine = mTimeLineAdapter.findItemById(event.getTimeLine().getTimelineId());
        if (timeLine == null) {
            return;
        }
        int position = mTimeLineAdapter.index(timeLine);
        mTimeLineAdapter.removeItem(position);
    }


}
