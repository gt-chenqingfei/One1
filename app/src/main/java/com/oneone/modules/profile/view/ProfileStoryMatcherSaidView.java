package com.oneone.modules.profile.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.FollowStatus;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.widget.SimpleRecyclerView;
import com.oneone.modules.matcher.relations.bean.MatcherInfo;
import com.oneone.modules.matcher.relations.ui.MatcherGroupActivity;
import com.oneone.modules.matcher.relations.ui.adapter.MatcherAdapter;
import com.oneone.modules.profile.contract.ProfileContract;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.widget.AvatarImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/5/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_profile_story_matcher_said)
public class ProfileStoryMatcherSaidView extends BaseView implements
        BaseViewHolder.ItemClickListener<MatcherInfo>, ProfileContract.OnFollowListener,
        ProfileContract.OnUnFollowListener {
    @BindView(R.id.layout_profile_story_matcher_recycler_view)
    SimpleRecyclerView<MatcherInfo> simpleRecyclerView;

    @BindView(R.id.layout_profile_story_matcher_tv_say_hi)
    TextView mTvSayHi;

    @BindView(R.id.layout_profile_story_matcher_header_rl)
    ViewGroup mLLFriendReleation;

    @BindView(R.id.layout_profile_story_matcher_said_add_follow_btn)
    TextView mTvFollow;

    @BindView(R.id.layout_profile_story_matcher_iv_avatar)
    AvatarImageView mIvAvatar;

    @BindView(R.id.layout_profile_story_matcher_btn_setting)
    ViewGroup mVGBtnMatcherRelationManager;
    @BindView(R.id.layout_profile_story_matcher_said_icon)
    View mIvFollowTip;

    MatcherAdapter mAdapter;
    private ProfilePresenter mProfilePresenter;
    private UserInfo mUserInfo;

    public ProfileStoryMatcherSaidView(Context context) {
        super(context);
    }

    public ProfileStoryMatcherSaidView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
        mAdapter = new MatcherAdapter(this, false);
        simpleRecyclerView.setAdapter(mAdapter);
    }

    public void notifyDataSetChange(List<MatcherInfo> matcherInfos) {
        mAdapter.notifyDataChange(matcherInfos);
        if (matcherInfos == null || matcherInfos.isEmpty()) {
            mLLFriendReleation.setVisibility(View.GONE);
        } else {
            mLLFriendReleation.setVisibility(View.VISIBLE);
            if (TextUtils.equals(mUserInfo.getUserId(), HereUser.getUserId())) {
                mVGBtnMatcherRelationManager.setVisibility(View.VISIBLE);
            }
        }
    }

    public void bindUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        this.mUserInfo = userInfo;

        mIvAvatar.init(userInfo, false);
        mTvSayHi.setText(String.format(getContext().getString(R.string.str_profile_this_is_me), userInfo.getNickname()));
        if (TextUtils.equals(userInfo.getUserId(), HereUser.getUserId())) {
            mIvFollowTip.setVisibility(View.GONE);
            mTvFollow.setVisibility(View.GONE);
        }
        refreshFollowState(userInfo);
    }

    private void refreshFollowState(UserInfo userInfo) {
        if (userInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW) {
            mTvFollow.setText(R.string.str_follow);
            mTvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_follow, 0, 0, 0);
        } else {
            mTvFollow.setText(R.string.str_had_follow);
            mTvFollow.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    @Override
    public void onItemClick(MatcherInfo matcherInfo, int id, int position) {

    }

    @OnClick(R.id.layout_profile_story_matcher_said_add_follow_btn)
    public void onFollowClick(View v) {
        UserInfo userInfo = mProfilePresenter.getUserInfo();
        if (userInfo.getFollow() == FollowStatus.STATUS_NO_FOLLOW) {
            mProfilePresenter.follow(userInfo.getUserId());
        } else if (userInfo.getFollow() == FollowStatus.STATUS_FOLLOW_YOU) {
            mProfilePresenter.unFollow(userInfo.getUserId());
        }
    }

    @OnClick(R.id.layout_profile_story_matcher_btn_setting)
    public void onRelationManagerClick(View v) {
        int count = UserSP.getInt(v.getContext(), Constants.PREF.PREF_MATCHER_GROUP_COUNT, 0);
        MatcherGroupActivity.startActivity(getContext(), count);
    }

    public void bindProfilePresenter(ProfilePresenter presenter) {
        mProfilePresenter = presenter;
        mProfilePresenter.addFollowAndUnFollowListener(this, this);
    }

    @Override
    public void onUnFollow(int followStatus) {
        refreshFollowState(mProfilePresenter.getUserInfo());
    }

    @Override
    public void onFollow(int followStatus) {
        refreshFollowState(mProfilePresenter.getUserInfo());
    }
}
