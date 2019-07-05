package com.oneone.modules.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.BaseViewHolder;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.imagepicker.util.Utils;
import com.oneone.framework.ui.imagepicker.view.RecyclerViewHorizontalItemDecoration;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.profile.adapter.AvatarImageViewAdapter;
import com.oneone.modules.profile.adapter.IntersectionTagAdapter;
import com.oneone.modules.profile.bean.IntersectionInfo;
import com.oneone.modules.profile.contract.IntersectionContract;
import com.oneone.modules.profile.presenter.IntersectionPresenter;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.activity.ModifySingleUserMainActivity;
import com.oneone.modules.user.bean.UserAvatarInfo;
import com.oneone.modules.user.bean.UserInfo;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/6/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, title = R.string.empty_str, background = R.color.transparent)
@LayoutResource(R.layout.activty_intersection)
public class IntersectionActivity extends BaseActivity<IntersectionPresenter, IntersectionContract.View>
        implements IntersectionContract.View, IntersectionContract.IntersectionListener,
        BaseViewHolder.ItemClickListener<UserAvatarInfo>, IntersectionContract.IntersectionUserInfoListener {

    @BindView(R.id.tv_intersection_num)
    TextView intersectionNumTextView;
    @BindView(R.id.tv_interaction_friend_num)
    TextView intersectionFriendNumTextView;
    @BindView(R.id.tv_interaction_matcher_num)
    TextView intersectionMatcherNumTextView;
    @BindView(R.id.recyclerview_friend)
    RecyclerView intersectionFriendPicRecyclerView;
    @BindView(R.id.recyclerview_matcher)
    RecyclerView intersectionMatcherPicRecyclerView;
    @BindView(R.id.recyclerview_tag)
    RecyclerView intersectionTagRecyclerView;

    public static final String EXTRA_USER_ID = "user_id";
    private String userId;
    private AvatarImageViewAdapter avatarImageViewAdapter;
    private IntersectionTagAdapter intersectionTagAdapter;

    public static void startActivity(Context context, String userId) {
        Intent it = new Intent(context, IntersectionActivity.class);
        it.putExtra(EXTRA_USER_ID, userId);
        context.startActivity(it);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        userId = intent.getStringExtra(EXTRA_USER_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.overridePendingTransition(R.anim.activity_in_from_bottom, R.anim.activity_none);
        mPresenter.getUserIntersectionInfo(userId, this);
        avatarImageViewAdapter = new AvatarImageViewAdapter(this);
        intersectionTagAdapter = new IntersectionTagAdapter();

        LinearLayoutManager linearLayoutManagerFriend = new LinearLayoutManager(this);
        linearLayoutManagerFriend.setOrientation(LinearLayoutManager.HORIZONTAL);
        intersectionFriendPicRecyclerView.setLayoutManager(linearLayoutManagerFriend);
        intersectionFriendPicRecyclerView.addItemDecoration(new RecyclerViewHorizontalItemDecoration(Utils.dp2px(this, 5)));
        intersectionFriendPicRecyclerView.setAdapter(avatarImageViewAdapter);

        LinearLayoutManager linearLayoutManagerMatcher = new LinearLayoutManager(this);
        linearLayoutManagerMatcher.setOrientation(LinearLayoutManager.HORIZONTAL);
        intersectionMatcherPicRecyclerView.setLayoutManager(linearLayoutManagerMatcher);
        intersectionMatcherPicRecyclerView.addItemDecoration(new RecyclerViewHorizontalItemDecoration(Utils.dp2px(this, 5)));
        intersectionMatcherPicRecyclerView.setAdapter(avatarImageViewAdapter);

        intersectionTagRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        intersectionTagRecyclerView.setAdapter(intersectionTagAdapter);
    }

    public IntersectionPresenter onCreatePresenter() {
        return new IntersectionPresenter();
    }

    @Override
    public void finish() {
        super.finish();
        super.overridePendingTransition(R.anim.activity_none, R.anim.activity_out_to_bottom);
    }

    @Override
    public void onIntersectionListener(IntersectionInfo intersectionInfo) {
        int friendCount = intersectionInfo.getFriendCount();
        int matcherCount = intersectionInfo.getMatcherCount();
        int tagCount = intersectionInfo.getTagCount();
        intersectionNumTextView.setText(friendCount + matcherCount + tagCount + "");
        if (friendCount > 0) {
            intersectionFriendPicRecyclerView.setVisibility(View.VISIBLE);
            intersectionFriendNumTextView.setVisibility(View.VISIBLE);
            intersectionFriendNumTextView.setText(String.format(getResources().getString(R.string.str_intersection_friend), friendCount));
            avatarImageViewAdapter.addData(intersectionInfo.getFriendList());
        }
        if (matcherCount > 0) {
            intersectionMatcherPicRecyclerView.setVisibility(View.VISIBLE);
            intersectionMatcherNumTextView.setVisibility(View.VISIBLE);
            intersectionMatcherNumTextView.setText(String.format(getResources().getString(R.string.str_intersection_matcher), matcherCount));
            avatarImageViewAdapter.addData(intersectionInfo.getMatcherList());
        }
        if (tagCount > 0) {
            intersectionTagAdapter.addData(intersectionInfo.getTagList());
        }
    }

    @Override
    public void onItemClick(UserAvatarInfo userAvatarInfo, int id, int position) {
        switch (id) {
            case R.id.iv_avatar:
                mPresenter.getUserInfo(userAvatarInfo.getUserId(), IntersectionActivity.this);
                break;
        }
    }

    @Override
    public void onIntersectionUserInfoListener(UserInfo userInfo) {
        if (userInfo.getUserId().equals(HereUser.getInstance().getUserId())) {
            ModifySingleUserMainActivity.startActivity(IntersectionActivity.this);
        } else {
            ProfileStater.startWithUserInfo(IntersectionActivity.this, userInfo);
        }
    }
}
