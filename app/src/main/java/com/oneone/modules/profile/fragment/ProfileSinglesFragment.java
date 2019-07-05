package com.oneone.modules.profile.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.MeasureViewPager;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.profile.adapter.ProfileSinglesAdapter;
import com.oneone.modules.profile.contract.ProfileSinglesContract;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.profile.presenter.ProfileSinglesPresenter;
import com.oneone.modules.profile.view.EmptyView4Singles;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.widget.NoMorelData4Common;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@SuppressLint("ValidFragment")
@LayoutResource(R.layout.frag_profile_singles)
public class ProfileSinglesFragment extends AbstractProfileFragment<ProfileSinglesPresenter, ProfileSinglesContract.View> implements ProfileSinglesContract.View {
    @BindView(R.id.layout_profile_singles_recycler_view)
    SimplePullRecyclerView<MatcherInfo> mSimpleRecyclerView;
    ProfileSinglesAdapter mSingleAdapter;
    private UserInfo mUserInfo;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mUserInfo != null) {
            mPresenter.getSingles(false, mUserInfo.getUserId());
        }
        mSingleAdapter = new ProfileSinglesAdapter(null, mUserInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSimpleRecyclerView.setAdapter(mSingleAdapter, layoutManager);
        mSimpleRecyclerView.setEmptyView(new EmptyView4Singles(getContext(), mUserInfo.getUserId()));
        mSimpleRecyclerView.setFooter(new NoMorelData4Common(getContext(), R.string.str_no_more_data_4_profile_singles));
        mSimpleRecyclerView.setRefreshEnable(false);
    }

    public ProfileSinglesFragment(MeasureViewPager pager, int position) {
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
        if (mUserInfo != null) {
            mPresenter.getSingles(false, mUserInfo.getUserId());
        }
    }

    @Override
    public ProfileSinglesPresenter onPresenterCreate() {
        return new ProfileSinglesPresenter();
    }

    @Override
    public void onGetSingles(List<SingleInfo> infoList, boolean isLoadMore, int count, boolean isLoadEnd) {

        if (isLoadMore) {
            mSingleAdapter.addData(infoList);
            mSimpleRecyclerView.setNoMoreData(isLoadEnd);
        } else {
            if (count > 0) {
                SingleInfo header = new SingleInfo();
                infoList.add(0, header);
                mSimpleRecyclerView.setNoMoreData(isLoadEnd);
            }
            mSingleAdapter.setTotalCount(count);
            mSingleAdapter.notifyDataChange(infoList);
        }
    }


    @Override
    public void onLoadMore() {
        if (mSimpleRecyclerView.isNoMoreData()) {
            return;
        }
        if (mPresenter == null) {
            return;
        }
        if (mSingleAdapter.getItemCount() <= 0) {
            return;
        }
        mPresenter.getSingles(true, mUserInfo.getUserId());
    }
}
