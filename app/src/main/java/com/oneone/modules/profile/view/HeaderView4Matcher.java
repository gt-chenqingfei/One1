package com.oneone.modules.profile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.FollowStatus;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ShareUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_profile_header_view_4_matcher)
public class HeaderView4Matcher extends AbstractProfileView implements ProfileContract.OnFollowListener, ProfileContract.OnUnFollowListener {

    @BindView(R.id.layout_profile_header_view_4_matcher_tv_follow)
    TextView mTvFollow;
    @BindView(R.id.layout_profile_header_view_4_matcher_tv_share)
    TextView mTvShare;
    @BindView(R.id.layout_profile_header_view_4_matcher_ll)
    LinearLayout mLLContainer;

    private ProfilePresenter mPresenter;
    private UserInfo mUserInfo;

    public HeaderView4Matcher(Context context) {
        super(context);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void bindData(UserInfo userInfo, ProfilePresenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.addFollowAndUnFollowListener(this, this);
        this.mUserInfo = userInfo;
        if (userInfo.getUserId().equals(HereUser.getInstance().getUserId())) {
            mLLContainer.setVisibility(View.INVISIBLE);
            LayoutParams params = (LayoutParams) mLLContainer.getLayoutParams();
            params.bottomMargin = 0;
            mLLContainer.setLayoutParams(params);
        }
        refreshFollowStatus(userInfo);
    }

    private void refreshFollowStatus(UserInfo userInfo) {
        if (userInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW) {
            mTvFollow.setText(R.string.str_follow);
            mTvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_follow, 0, 0, 0);
        } else {
            mTvFollow.setText(R.string.str_had_follow);
            mTvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }
    }


    @OnClick(R.id.layout_profile_header_view_4_matcher_ll_share)
    public void onShareClick(View v) {
        new ShareUtil(getContext(), mUserInfo).show();
    }

    @OnClick(R.id.layout_profile_header_view_4_matcher_ll_follow)
    public void onFollowClick(View v) {
        if (mUserInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW) {
            mPresenter.follow(mUserInfo.getUserId());
        } else if (mUserInfo.getFollow() == FollowStatus.STATUS_FOLLOW_YOU) {
            mPresenter.unFollow(mUserInfo.getUserId());
        }
    }

    @Override
    public void onFollow(int followStatus) {
        refreshFollowStatus(mUserInfo);
    }

    @Override
    public void onUnFollow(int followStatus) {
        refreshFollowStatus(mUserInfo);
    }
}
