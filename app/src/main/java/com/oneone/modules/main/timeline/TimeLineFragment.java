package com.oneone.modules.main.timeline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.api.constants.Role;
import com.oneone.event.EventTimelineDelete;
import com.oneone.event.EventTimelineLike;
import com.oneone.event.EventTimelinePost;
import com.oneone.framework.android.utils.TimeUtils;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.dogfood.activity.MyDogFoodActivity;
import com.oneone.modules.profile.view.EmptyView4Timeline;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.timeline.NewTimeLineManager;
import com.oneone.modules.timeline.activity.TimeLinePostActivity;
import com.oneone.modules.timeline.adapter.TimeLineAdapter;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.presenter.TimeLineHomePresenter;
import com.oneone.modules.timeline.view.TimeLineFloatingMenu;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.utils.TimeUtil;
import com.oneone.widget.CustomGlobalFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/3/31.
 */
@LayoutResource(R.layout.frag_timeline)
public class TimeLineFragment extends BaseMainFragment<TimeLineHomePresenter, TimeLineContract.View>
        implements TimeLineContract.View, BaseViewHolder.ItemClickListener<TimeLine>,
        OnRefreshLoadMoreListener,
        TimeLineContract.OnGetHomeTimeLineListener
        , SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.frag_timeline_toolbar)
    ViewGroup mToolbar;
    @BindView(R.id.frag_timeline_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.frag_timeline_toolbar_title_time)
    TextView mTvSubTitle;
    @BindView(R.id.frag_timeline_toolbar_title_tv_dog_foot)
    TextView mTvBalance;

    @BindView(R.id.frag_timeline_toolbar_title_ll_dog_foot)
    ViewGroup mLLBalanceContiner;

    @BindView(R.id.fragment_timeline_simple_pull_rv_recycler_view)
    SimplePullRecyclerView<TimeLine> mRecyclerView;

    @BindView(R.id.frag_timeline_floating_action_button)
    TimeLineFloatingMenu mTimelineFloatMenu;

    private LinearLayoutManager mLinearLayoutManager;
    private TimeLineAdapter mTimeLineAdapter;
    private boolean isFirstLoadEmptyData;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTimeLineAdapter = new TimeLineAdapter(this, getContext(), mPresenter);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mTimeLineAdapter, mLinearLayoutManager);
        mRecyclerView.setOnRefreshLoadMoreListener(this);
        mRecyclerView.setEmptyView(new EmptyView4Timeline(getContext()));
        mRecyclerView.autoRefresh();
        refreshTimelinePostBtn();
        EventBus.getDefault().register(this);
        UserSP.getInstance().registerListener(getContext(), this);
        refreshBalance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        UserSP.getInstance().unregisterListener(getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDate();
        boolean isShowDot = RedDotManager.getInstance().isShowNewTimelineDot();
        showDot(isShowDot);
    }

    @Override
    public TimeLineHomePresenter onPresenterCreate() {
        return new TimeLineHomePresenter();
    }

    @Override
    public void onAppear() {
        super.onAppear();
        boolean isShowDot = RedDotManager.getInstance().isShowNewTimelineDot();
        if (isShowDot) {
            mRecyclerView.autoRefresh();
        }
    }

    @Override
    public View getTitleView() {
        return mToolbar;
    }

    @Override
    public void onTabDoubleTap() {
        super.onTabDoubleTap();
        mRecyclerView.getRecyclerView().scrollToPosition(0);
        mRecyclerView.autoRefresh();
    }

    @Override
    public void onItemClick(TimeLine timeLine, int id, int position) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mRecyclerView.isNoMoreData()) {
            ((CustomGlobalFooter) refreshLayout.getRefreshFooter()).
                    setSpecifyFooterView(R.string.refresh_footer_nomore_data_timeline);
            mRecyclerView.getSmartRefreshLayout().finishLoadMore();
            return;
        }
        if (isFirstLoadEmptyData) {
            mRecyclerView.getSmartRefreshLayout().setEnableLoadMore(false);
            mRecyclerView.getSmartRefreshLayout().finishLoadMore();
            isFirstLoadEmptyData = false;
        }
        if (mPresenter == null) {
            return;
        }
        if (mTimeLineAdapter.getItemCount() <= 0) {
            return;
        }
        TimeLine line = mTimeLineAdapter.getItem(mTimeLineAdapter.getItemCount() - 1);
        if (line != null) {
            mPresenter.getHomeTimeLine(line.getTimelineId(), this);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getHomeTimeLine(0, this);
        mRecyclerView.getSmartRefreshLayout().setEnableLoadMore(true);
        RedDotManager.getInstance().clearDot(RedDot.NEW_TIMELINE);
        ((CustomGlobalFooter) refreshLayout.getRefreshFooter()).setFooterLoadingView();
    }

    private void setScrollListener() {
        mRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int scrollY) {
                super.onScrolled(recyclerView, dx, scrollY);
                int toolBarHeight = mToolbar.getMeasuredHeight();
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                float percent = (scrollOffset - toolBarHeight) / (toolBarHeight * 1.5F);
                if (percent < 0) {
                    percent = 0;
                } else if (percent > 1) {
                    percent = 1;
                }
                if (scrollOffset >= toolBarHeight * 2.5) {
                    mToolbar.setAlpha(1);
                } else if (scrollOffset >= toolBarHeight) {
                    if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mToolbar.setAlpha(percent);
                    }
                } else {
                    mToolbar.setAlpha(0);
                }
            }
        });
    }


    @Override
    public void onGetHomeTimeLine(List<TimeLine> timeLines, boolean isLoadMore, boolean isLoadEnd) {
        List<TimeLine> unSendTimeLine = HereSingletonFactory.getInstance().getNewTimeLineManager().getTimeLineSendWaitList();
        if (null == timeLines ||  timeLines != null && timeLines.isEmpty()) {
            if (mTimeLineAdapter.getItemCount() != 0 && timeLines != null && timeLines.isEmpty()) {// 最后一页
                mRecyclerView.setNoMoreData(isLoadEnd);
            }
            mRecyclerView.onLoadFailed("");
            timeLines = mTimeLineAdapter.getList();
            if (timeLines != null && unSendTimeLine != null) {
                for(TimeLine timeLine:unSendTimeLine){
                    timeLines.remove(timeLine);
                }
                mTimeLineAdapter.notifyDataChange(timeLines);
            }
            if (mTimeLineAdapter.getItemCount() == 0 && timeLines.isEmpty()) {// 空页面
                isFirstLoadEmptyData = true;
            }

            return;
        }
        timeLines.addAll(0, unSendTimeLine);
        mRecyclerView.setNoMoreData(isLoadEnd);
        if (isLoadMore) {
            mRecyclerView.onLoadMoreCompleted(timeLines);
        } else {
            mRecyclerView.onRefreshCompleted(timeLines);
        }
    }

    @OnClick(R.id.frag_timeline_toolbar_title_ll_dog_foot)
    public void onDogFootClick(View view) {
        MyDogFoodActivity.startActivity(getContext());
    }


    @OnClick(R.id.frag_timeline_fab_menu_text)
    public void onItemNewTextMomentClick(View v) {
        mTimelineFloatMenu.collapse();
        TimeLineContract.INewTimeLineManager timeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        if (timeLineManager.isSendingTimeLine4Text()) {
            return;
        }
        TimeLinePostActivity.startPostTextActivity(getContext());
    }

    @OnClick(R.id.frag_timeline_fab_menu_image_text)
    public void onItemNewImageTextMomentClick(View v) {
        mTimelineFloatMenu.collapse();
        TimeLineContract.INewTimeLineManager timeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        if (timeLineManager.isSendingTimeLine4ImageText()) {
            return;
        }
        TimeLinePostActivity.startPostImageTextActivity(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLineLikeEvent(EventTimelineLike event) {
        TimeLine timeLine = mTimeLineAdapter.findItemById(event.getTimeLine().getTimelineId());
        if (timeLine == null) {
            return;
        }
        timeLine.setLikeTypes(event.getTimeLine().getLikeTypes());
        timeLine.setMyLikeType(event.getTimeLine().getMyLikeType());
        int position = mTimeLineAdapter.index(timeLine);
        if (position != -1) {
            mTimeLineAdapter.notifyItemChanged(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLineDeleteEvent(EventTimelineDelete event) {
        if (event.isClear()) {
            mRecyclerView.autoRefresh();
            return;
        }
        TimeLine timeLine = mTimeLineAdapter.findItemById(event.getTimeLine().getTimelineId());
        if (timeLine == null) {
            return;
        }
        int position = mTimeLineAdapter.index(timeLine);
        mTimeLineAdapter.removeItem(position);

        if (event.getPosition() == 0) {
            mTimeLineAdapter.notifyItemChanged(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLinePostEvent(EventTimelinePost event) {
        switch (event.getStatus()) {
            case EventTimelinePost.STATUS_SEND_EXCEPTION:
                mTimeLineAdapter.notifyItemChanged(0);
                break;
            case EventTimelinePost.STATUS_SEND_SUCCESS:
                mRecyclerView.autoRefresh();
                break;
            case EventTimelinePost.STATUS_SENDING:
                NewTimeLineManager timeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
                if (timeLineManager != null) {
                    mTimeLineAdapter.addData(timeLineManager.
                            getTimeLineSendWaitList(), 0);
                }
                break;

        }
    }

    private void setDate() {
        mTvTitle.setText(TimeUtil.getTimeIntervalOf24Hour());
        String week = TimeUtils.getChineseWeek(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String date = TimeUtils.getNowString(format);
        mTvSubTitle.setText(date + ", " + week);
    }

    private void refreshBalance() {
        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
            mLLBalanceContiner.setVisibility(View.GONE);
        }
        int balance = UserSP.getInt(getContext(), UserSP.DOG_FOOT_BALANCE, 0);
        mTvBalance.setText(balance + "");
    }

    private void refreshTimelinePostBtn() {
        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
            mTimelineFloatMenu.setVisibility(View.GONE);
        } else {
            mTimelineFloatMenu.attachToRecyclerView(mRecyclerView.getRecyclerView());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(RedDotManager.PREF_DOT + RedDot.NEW_TIMELINE)) {
            showDot(RedDotManager.getInstance().isShowNewTimelineDot());
        } else if (key.equals(UserSP.DOG_FOOT_BALANCE)) {
            refreshBalance();
        }
    }
}
