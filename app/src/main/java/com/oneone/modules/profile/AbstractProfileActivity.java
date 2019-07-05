package com.oneone.modules.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.framework.ui.widget.DampingScrollView;
import com.oneone.framework.ui.widget.MeasureViewPager;
import com.oneone.framework.ui.widget.tablayout.SlidingTabLayout;
import com.oneone.modules.profile.adapter.TabLayoutPagerAdapter;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.modules.profile.fragment.AbstractProfileFragment;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.profile.view.AbstractProfileView;
import com.oneone.modules.user.bean.UserInfo;

import java.util.List;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/5/9.
 */

public abstract class AbstractProfileActivity extends BaseActivity<ProfilePresenter, ProfileContract.View>
        implements ViewPager.OnPageChangeListener, DampingScrollView.OnScrollListener, ProfileContract.View,
        DampingScrollView.OnRefreshListener, DampingScrollView.OnLoadMoreListener {

    public static String EXTRA_USER_INFO = "extra_user_info";
    public static String EXTRA_USER_ID = "extra_user_id";
    public static String EXTRA_TAB = "tab";

    @BindView(R.id.activity_profile_dumpling_container)
    ViewGroup mProfileDumpingImageContainer;

    @BindView(R.id.widget_toolbar_bg)
    View mToolbarBg;
    @BindView(R.id.widget_toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.activity_profile_header)
    FrameLayout mProfileHeaderContainer;

    @BindView(R.id.activity_profile_viewpager)
    MeasureViewPager mViewPager;

    @BindView(R.id.activity_profile_sliding_tab_layout)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.activity_profile_sliding_tab_container)
    LinearLayout mSlidingTabLayoutContainer;

    @BindView(R.id.activity_profile_damping_scroll_view)
    DampingScrollView mScrollView;

    private int mMarginTop;
    private int mMeasuredHeight = 0;

    AbstractProfileView mHeaderView;
    AbstractProfileView mDumpingView;

    UserInfo userInfo;
    protected String userId;
    List<AbstractProfileFragment> mFragments;

    public abstract String[] getTitles();

    public abstract AbstractProfileView getHeaderView();

    public abstract AbstractProfileView getDumpingChildView();

    public abstract List<AbstractProfileFragment> getFragments();

    public abstract int getInitTabPosition(String tab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.overridePendingTransition(R.anim.activity_in_from_bottom, R.anim.activity_none);
        initView();
        mToolbarBg.setBackgroundColor(getToolbarBackgroundColor());
        String tab = getIntent().getStringExtra(EXTRA_TAB);
        if (!TextUtils.isEmpty(tab)) {
            mViewPager.setCurrentItem(getInitTabPosition(tab));
        }
        bindUser(userInfo);
        mToolbarTitle.setText(userInfo.getNickname());
    }

    @Override
    public void finish() {
        super.finish();
        super.overridePendingTransition(R.anim.activity_none, R.anim.activity_out_to_bottom);
    }


    @Override
    public ProfilePresenter onCreatePresenter() {
        return new ProfilePresenter();
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        userId = intent.getStringExtra(EXTRA_USER_ID);
        if (intent.getParcelableExtra(EXTRA_USER_INFO) != null) {
            userInfo = intent.getParcelableExtra(EXTRA_USER_INFO);
        }
    }

    @Override
    public void onUserInfoGet(UserInfo userInfo) {
        bindUser(userInfo);
    }

    private void bindUser(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mHeaderView.bindData(userInfo, mPresenter);
        mDumpingView.bindData(userInfo, mPresenter);

        for (AbstractProfileFragment fragment : mFragments) {
            fragment.bindUserInfo(userInfo);
            fragment.bindProfilePresenter(mPresenter);
        }

    }

    public MeasureViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.resetHeight(position);
    }

    @Override
    public void onScroll(int scrollY) {
        if (mMeasuredHeight == 0) {
            mMeasuredHeight = mProfileDumpingImageContainer.getMeasuredHeight() + mMarginTop;
        }
        int viewPagerTopDistance = mViewPager.getTop() + mMeasuredHeight;

        int translation;
        if (scrollY < 0) {
            translation = Math.max(scrollY, viewPagerTopDistance - scrollY);
        } else {
            translation = Math.max(scrollY, viewPagerTopDistance);
            int mTitleAlpha = scrollY;
//            mTitleAlpha = scrollY - viewPagerTopDistance;
            if (mTitleAlpha >= 255) {
                mTitleAlpha = 255;
                getToolbar().setBackgroundColor(getToolbarBackgroundColor());
                mToolbarTitle.setVisibility(View.VISIBLE);
            } else {
                getToolbar().setBackgroundColor(getResources().getColor(R.color.color_transparent));
                mToolbarTitle.setVisibility(View.GONE);
            }
            mToolbarBg.setAlpha((float) (mTitleAlpha / 255.0));
        }
        mSlidingTabLayoutContainer.setTranslationY(translation);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initView() {
        int mStatusBarHeight = ScreenUtil.getStatusBarHeight(this);
        int mViewPagerContentMinHeight = (int) (ScreenUtil.screenHeight -
                mStatusBarHeight - getResources().getDimension(R.dimen.dimen_dp_128));

        int titleAndToolbarHeight = (int) getResources().getDimension(R.dimen.dimen_dp_82);
        int SlidingTabLayoutHeight = (int) getResources().getDimension(R.dimen.dimen_dp_48);
        int floatHeight = (int) getResources().getDimension(R.dimen.dimen_dp_negative_32);

        mMarginTop = floatHeight - SlidingTabLayoutHeight - titleAndToolbarHeight;

        String mTitles[] = getTitles();
        mFragments = getFragments();
        TabLayoutPagerAdapter mAdapter = new TabLayoutPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setAdaptive(false);
        mViewPager.setScrollble(false);
        mViewPager.setMinHeight(mViewPagerContentMinHeight);
        mSlidingTabLayout.setTabSpaceEqual(true);
        mSlidingTabLayout.setViewPager(mViewPager, mTitles);
        mViewPager.setOffscreenPageLimit(mTitles.length);

        ImageView dumpingBgImageView = null;

        mDumpingView = getDumpingChildView();
        if (mDumpingView != null) {
            dumpingBgImageView = mDumpingView.findViewById(R.id.activity_profile_dumping_image);
        }

        mProfileDumpingImageContainer.addView(mDumpingView);
        mScrollView.setImageView(dumpingBgImageView);

        mHeaderView = getHeaderView();
        mProfileHeaderContainer.addView(mHeaderView);

        mViewPager.setScrollble(true);
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.resetScrollView();
            }
        }, 500);

        mViewPager.addOnPageChangeListener(this);
        mScrollView.setOnScrollListener(this);
        mScrollView.setOnPullRefreshListener(this);
        mScrollView.setOnLoadMoreListener(this);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void onPullRefresh() {
        if (!TextUtils.isEmpty(userId)) {
            mPresenter.getUserInfo(userId);
        }

        for (AbstractProfileFragment fragment : mFragments) {
            fragment.onPullRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        for (AbstractProfileFragment fragment : mFragments) {
            if (fragment.getPosition() == mViewPager.getCurrentItem()) {
                fragment.onLoadMore();
            }
        }
    }
}
