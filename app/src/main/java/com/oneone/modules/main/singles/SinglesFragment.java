package com.oneone.modules.main.singles;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.RedDot;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.matcher.relations.contract.SinglesContract;
import com.oneone.modules.matcher.relations.presenter.SinglesPresenter;
import com.oneone.modules.matcher.relations.ui.MatcherRelationSetActivity;
import com.oneone.modules.matcher.relations.ui.SinglesInviteActivity;
import com.oneone.modules.matcher.relations.ui.adapter.SingleAdapter;
import com.oneone.modules.msg.activity.ClientAndSystemMsgActivity;
import com.oneone.modules.profile.view.EmptyView4Singles;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.widget.CustomGlobalFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/3/31.
 */
@LayoutResource(R.layout.frag_singles)
public class SinglesFragment extends BaseMainFragment<SinglesPresenter, SinglesContract.View>
        implements SinglesContract.View,
        OnRefreshLoadMoreListener, View.OnClickListener,
        BaseViewHolder.ItemClickListener<SingleInfo>
        , SharedPreferences.OnSharedPreferenceChangeListener,
        SinglesContract.OnMySinglesGetListener {
    @BindView(R.id.frag_singles_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.frag_singles_toolbar_container)
    View mToolbarContianer;

    @BindView(R.id.frag_singles_toolbar_title)
    TextView mTvTitle;

    @BindView(R.id.frag_singles_toolbar_back_btn)
    ImageView mIvBackBtn;

    @BindView(R.id.frag_singles_toolbar_right_menu)
    ImageView mIvMenuMsg;
    @BindView(R.id.frag_singles_toolbar_right_menu_bubble)
    TextView mTvRightMenuBubble;

    @BindView(R.id.frag_singles_simple_pull_rv_recycler_view)
    SimplePullRecyclerView<SingleInfo> recyclerView;

    @Override
    public SinglesPresenter onPresenterCreate() {
        return new SinglesPresenter();
    }

    LinearLayoutManager mLinearLayoutManager;
    private SingleAdapter mAdapter;
    private boolean isBackBtnEnable = false;
    private boolean isRightMenuEnable = true;
    private int count = 0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SingleAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(mAdapter, mLinearLayoutManager);
        recyclerView.getSmartRefreshLayout().setOnRefreshLoadMoreListener(this);
        recyclerView.setEmptyView(new EmptyView4Singles(getContext(), HereUser.getUserId()));
        setScrollListener();
        mAdapter.addItem(new SingleInfo(), 0);

        if (isBackBtnEnable) {
            mIvBackBtn.setOnClickListener(this);
            mIvBackBtn.setVisibility(View.VISIBLE);
        } else {
            mIvBackBtn.setVisibility(View.GONE);
        }

        if (isRightMenuEnable) {
            mIvMenuMsg.setOnClickListener(this);
            mIvMenuMsg.setVisibility(View.VISIBLE);
        } else {
            mIvMenuMsg.setVisibility(View.GONE);
        }
        UserSP.getInstance().registerListener(getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setBubbleValue();
        mPresenter.getMySingles(false, this);
    }

    private void setBubbleValue() {
        if (!isRightMenuEnable) {
            return;
        }
        int bubble = RedDotManager.getInstance().getNewSinglesUnread();
        mTvRightMenuBubble.setText(getBubbleStr(bubble));
        mTvRightMenuBubble.setVisibility(bubble > 0 ? View.VISIBLE : View.GONE);
        setBubble(bubble);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserSP.getInstance().unregisterListener(getContext(), this);
    }

    @Override
    public View getTitleView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        return mToolbarContianer;
    }

    @Override
    public void onTabDoubleTap() {
        super.onTabDoubleTap();
        recyclerView.getSmartRefreshLayout().autoRefresh();
    }

    public void enableBackBtn(boolean enable) {
        this.isBackBtnEnable = enable;
    }

    public void enableRightMenu(boolean enable) {
        this.isRightMenuEnable = enable;
    }

    public void setSingleCount(int count) {
        this.count = count;
    }

    @Override
    public void onMySinglesGet(boolean isLoadMore, List<SingleInfo> singles, int count) {
        if (singles == null || singles.isEmpty()) {
            recyclerView.onLoadFailed("");
            return;
        }

        if (isBackBtnEnable) {
            RedDotManager.getInstance().clearDot(RedDot.NEW_SINGLES);
        }

        if (mAdapter.getItemCount() >= count || singles.size() == count) {
            recyclerView.setNoMoreData(true);
        }
        if (isLoadMore) {
            recyclerView.getSmartRefreshLayout().finishLoadMore();
            mAdapter.addData(singles);
        } else {
            mAdapter.setCount(count);
            recyclerView.getSmartRefreshLayout().setNoMoreData(false);
            recyclerView.getSmartRefreshLayout().finishRefresh();
            mAdapter.notifyDataChange(singles);
            mAdapter.addItem(new SingleInfo(), 0);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (recyclerView.isNoMoreData()) {
            ((CustomGlobalFooter) refreshLayout.getRefreshFooter()).
                    setSpecifyFooterView(R.string.refresh_footer_nomore_data_single);
            recyclerView.getSmartRefreshLayout().finishLoadMore();
        }
        mPresenter.getMySingles(true, this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ((CustomGlobalFooter) refreshLayout.getRefreshFooter()).setFooterLoadingView();
        mPresenter.getMySingles(false, this);
    }

    private void setScrollListener() {
        recyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {

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

                if (count <= 3 && isBackBtnEnable) {
                    mTvTitle.setText("");
                    mToolbar.setAlpha(1);
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                    return;
                }

                if (scrollOffset >= toolBarHeight * 2.5) {
                    mToolbar.setAlpha(1);
                    mToolbarContianer.setBackgroundColor(getResources().getColor(R.color.color_796CF0));
                    mToolbarContianer.setAlpha(1);
                } else if (scrollOffset >= toolBarHeight) {
                    if (mLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mToolbar.setAlpha(percent);
                        mToolbarContianer.setAlpha(percent);
                    }
                } else {
                    mToolbar.setAlpha(0);
                    mToolbarContianer.setAlpha(0);
                    mToolbarContianer.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_singles_toolbar_back_btn:
                getActivity().finish();
                break;

            case R.id.frag_singles_toolbar_right_menu:
                ClientAndSystemMsgActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    public void onItemClick(SingleInfo singleInfo, int id, int position) {
        if (id == R.id.item_singles_header_btn_invite) {
            SinglesInviteActivity.startActivity(getContext());
        } else if (id == R.id.item_singles_tv_matcher_relations_edit) {
            MatcherRelationSetActivity.launch(getContext(), singleInfo.getUserInfo().getUserId(),
                    singleInfo.getUserInfo().getMyNickname(), singleInfo.getMatcherSaid(), singleInfo.getRelationship());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(RedDotManager.PREF_DOT + RedDot.NEW_SINGLES)) {
            setBubbleValue();
        }
    }
}
