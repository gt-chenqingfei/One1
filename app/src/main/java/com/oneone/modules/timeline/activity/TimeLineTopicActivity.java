package com.oneone.modules.timeline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.oneone.BasePullListActivity;
import com.oneone.HereSingletonFactory;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventTimelineDelete;
import com.oneone.event.EventTimelineLike;
import com.oneone.event.EventTimelinePost;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.timeline.NewTimeLineManager;
import com.oneone.modules.timeline.adapter.TopicAdapter;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.timeline.bean.TimeLineTopic;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.modules.timeline.presenter.TimeLineTopicPresenter;
import com.oneone.modules.timeline.view.TimeLineFloatingMenu;
import com.oneone.modules.user.HereUser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 话题页面
 * <p>
 * Created by ZhaiDongyang on 2018/6/20
 */
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, title = R.string.empty_str, background = R.color.color_796CF0)
@LayoutResource(R.layout.activity_topic)
public class TimeLineTopicActivity extends BasePullListActivity<TimeLine, TimeLineTopicPresenter, TimeLineContract.View>
        implements TimeLineContract.View, TimeLineContract.OnGetTopicListener, BaseViewHolder.ItemClickListener<TimeLine> {

    private static final String TOPIC_NAME = "topic_name";
    private String topicName;
    private TopicAdapter mTopicAdapter;
    private TimeLineTopic operationTagInfo;

    @BindView(R.id.topic_recyclerView)
    SimplePullRecyclerView<TimeLineContainerDTO> mSimpleRecyclerView;
    @BindView(R.id.frag_timeline_floating_action_button)
    TimeLineFloatingMenu mTimelineFloatMenu;

    public static void startActivity(Context context, String topicName) {
        Intent intent = new Intent(context, TimeLineTopicActivity.class);
        intent.putExtra(TOPIC_NAME, topicName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mSimpleRecyclerView.getSmartRefreshLayout().autoRefresh();
        setScrollListener();
        refreshTimelinePostBtn();
    }

    private void refreshTimelinePostBtn() {
        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
            mTimelineFloatMenu.setVisibility(View.GONE);
        } else {
            mTimelineFloatMenu.attachToRecyclerView(mSimpleRecyclerView.getRecyclerView());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleIntent(Intent intent) {
        topicName = getIntent().getStringExtra(TOPIC_NAME);
        topicName = topicName.split("#")[1];
    }

    @Override
    protected boolean loadRefreshOnStart() {
        return false;
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter adapterToDisplay() {
        mTopicAdapter = new TopicAdapter(this, operationTagInfo, topicName, mPresenter);
        return mTopicAdapter;
    }

    @NonNull
    @Override
    public SimplePullRecyclerView getDisplayView() {
        mTimelineFloatMenu.attachToRecyclerView(mSimpleRecyclerView.getRecyclerView());
        return mSimpleRecyclerView;
    }

    @Override
    public TimeLineTopicPresenter onCreatePresenter() {
        return new TimeLineTopicPresenter();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mSimpleRecyclerView.isNoMoreData()) {
            return;
        }
        if (mPresenter == null) {
            return;
        }
        if (mTopicAdapter.getItemCount() <= 0) {
            return;
        }
        TimeLine line = mTopicAdapter.getItem(mTopicAdapter.getItemCount() - 1);
        if (line != null) mPresenter.getTopic(topicName, line.getTimelineId(), this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getTopic(topicName, 0, this);
    }

    @Override
    public void onGetTopicListener(TimeLineContainerDTO timeLineContainerDTOS, boolean isLoadMore, boolean isLoadEnd) {
        if (timeLineContainerDTOS == null || timeLineContainerDTOS.getList().isEmpty()) {
            onLoadFailed("");
            return;
        }
        operationTagInfo = timeLineContainerDTOS.getOperationTagInfo();
        if (isLoadMore) {
            onLoadMoreCompleted(timeLineContainerDTOS.getList());
        } else {
            onRefreshCompleted(timeLineContainerDTOS.getList());
            addHeader();
            addSendFailData();
        }
        mTopicAdapter.setmOperationTagInfo(operationTagInfo);
        setToolBarTitle();
    }

    private void addHeader() {
        mTopicAdapter.addItem(new TimeLine(), 0);
    }

    @Override
    public void onItemClick(TimeLine timeLine, int id, int position) {
        switch (id) {
            case R.id.item_topic_header_rl_text:
                break;
            case R.id.item_topic_header_topic_resend:
                TimeLineUnSendActivity.startActivity(this);
                break;
        }
    }


    @OnClick(R.id.frag_timeline_fab_menu_text)
    public void onItemNewTextMomentClick(View v) {
        mTimelineFloatMenu.collapse();
        NewTimeLineManager timeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        if (timeLineManager.isSendingTimeLine4Text()) {
            return;
        }
        TimeLinePostActivity.startPostTextWithTopicActivity(this, topicName);
    }

    @OnClick(R.id.frag_timeline_fab_menu_pic)
    public void onItemNewPicMomentClick(View v) {
        mTimelineFloatMenu.collapse();
        NewTimeLineManager timeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        if (timeLineManager.isSendingTimeLine4ImageText()) {
            return;
        }
        TimeLinePostActivity.startPostImageTextWithTopicActivity(this, topicName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLineLikeEvent(EventTimelineLike event) {
        TimeLine timeLine = mTopicAdapter.findItemById(event.getTimeLine().getTimelineId());
        if (timeLine == null) {
            return;
        }
        int position = mTopicAdapter.index(timeLine);
        if (position != -1) {
            mTopicAdapter.notifyItemChanged(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLineDeleteEvent(EventTimelineDelete event) {
        TimeLine timeLine = mTopicAdapter.findItemById(event.getTimeLine().getTimelineId());
        if (timeLine == null) {
            return;
        }
        int position = mTopicAdapter.index(timeLine);
        mTopicAdapter.removeItem(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTimeLinePostEvent(EventTimelinePost event) {
        switch (event.getStatus()) {
            case EventTimelinePost.STATUS_SEND_EXCEPTION:
                mTopicAdapter.notifyItemChanged(1);
                break;
            case EventTimelinePost.STATUS_SEND_SUCCESS:
                mSimpleRecyclerView.autoRefresh();
                break;
            case EventTimelinePost.STATUS_SENDING:
                addSendFailData();
                break;
        }
    }

    private void addSendFailData() {
        NewTimeLineManager timeLineManager = HereSingletonFactory.getInstance().getNewTimeLineManager();
        if (timeLineManager != null) {
            mTopicAdapter.addData(timeLineManager.
                    getTimeLineSendWaitList(), 1);
            mTopicAdapter.setHasSendFailedData(timeLineManager.getTimeLineUnSendList() == null
                    || timeLineManager.getTimeLineUnSendList().isEmpty());
        }
    }

    private void setScrollListener() {
        mSimpleRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int scrollY) {
                super.onScrolled(recyclerView, dx, scrollY);
                int toolBarHeight = getToolbar().getMeasuredHeight();
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                float percent = (scrollOffset - toolBarHeight) / (toolBarHeight * 1.5F);
                if (percent < 0) {
                    percent = 0;
                } else if (percent > 1) {
                    percent = 1;
                }
                if (scrollOffset >= toolBarHeight * 2.5) {
                    getToolbar().setAlpha(1);
                    getMiddleTitle().setAlpha(1);
                } else if (scrollOffset >= toolBarHeight) {
                    if (((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                        getToolbar().setAlpha(percent);
                    }
                    getMiddleTitle().setAlpha(percent);
                } else {
                    getToolbar().setAlpha(1);
                    getMiddleTitle().setAlpha(0);
                }
            }
        });
    }

    private void setToolBarTitle() {
        final TextView textView = getMiddleTitle();
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText("#" + topicName + "#");
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getAlpha() == 1) {
                    mSimpleRecyclerView.getRecyclerView().scrollToPosition(0);
                    mSimpleRecyclerView.getSmartRefreshLayout().autoRefresh();
                }
            }
        });
    }

}
