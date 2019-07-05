package com.oneone.modules.main.me.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.following.activity.MyFollowingActivity;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserSP;
import com.oneone.modules.user.activity.ModifyMatcherUserBasicActivity;
import com.oneone.modules.user.activity.ModifySingleUserMainActivity;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserStatisticInfo;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.view_mine_top_view)
public class MineSummary extends BaseView implements View.OnClickListener
        , SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.user_photo_iv)
    AvatarImageView userPhotoIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.user_company_and_title_tv)
    TextView userDescTv;
    @BindView(R.id.user_info_modify_btn)
    Button userInfoModifyBtn;
    @BindView(R.id.user_matcher_tag_iv)
    ImageView matcherTagIv;

    @BindView(R.id.dynamic_layout)
    RelativeLayout dynamicLayout;
    @BindView(R.id.attention_layout)
    RelativeLayout attentionLayout;
    @BindView(R.id.be_attention_layout)
    RelativeLayout beAttentionLayout;
    @BindView(R.id.dynamic_count_tv)
    TextView dynamicCountTv;
    @BindView(R.id.attention_count_tv)
    TextView attentionCountTv;
    @BindView(R.id.be_attention_count_tv)
    TextView beAttentionCountTv;

    private Context context;
    private UserInfo mUserInfo;

    private MineSummaryOnClickListener listener;

    public void setClickListener(MineSummaryOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(HereUser.class.getSimpleName())) {
            bindData(HereUser.getInstance().getUserInfo());
        }
    }

    public interface MineSummaryOnClickListener {
        void onEditClick();
    }

    public MineSummary(Context context) {
        super(context);
        this.context = context;
    }

    public MineSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void onViewCreated() {
        userInfoModifyBtn.setOnClickListener(this);
        dynamicLayout.setOnClickListener(this);
        attentionLayout.setOnClickListener(this);
        beAttentionLayout.setOnClickListener(this);
        this.setOnClickListener(this);
        userPhotoIv.setOnClickListener(this);
        bindData(HereUser.getInstance().getUserInfo());
        DefaultSP.getInstance().registerListener(getContext(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DefaultSP.getInstance().unregisterListener(context, this);
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            ProfileStater.startWithUserInfo(v.getContext(), HereUser.getInstance().getUserInfo());
            return;
        }
        switch (v.getId()) {
            case R.id.attention_layout:
                MyFollowingActivity.startActivity(context, true);
                break;

            case R.id.be_attention_layout:
                MyFollowingActivity.startActivity(context, false);
                break;

            case R.id.user_info_modify_btn:
                if (listener != null) {
                    listener.onEditClick();
                }
                break;

            case R.id.dynamic_layout:
                ProfileStater.startWithUserInfo(v.getContext(), HereUser.getInstance().getUserInfo(), ProfileStater.TAB_TIMELINE);
                break;

            case R.id.user_photo_iv:
                if (mUserInfo.getUserId().equals(HereUser.getUserId())) {
                    if (mUserInfo.getRole() == Role.SINGLE) {
                        ModifySingleUserMainActivity.startActivity(getContext());
                    } else {
                        ModifyMatcherUserBasicActivity.startActivity(getContext());
                    }
                }
                break;
        }
    }

    public void setUserStatisticInfo(UserStatisticInfo userStatisticInfo) {
        if (userStatisticInfo != null) {
            dynamicCountTv.setText(userStatisticInfo.getCountTimeline().getCount() + "");
            attentionCountTv.setText(userStatisticInfo.getCountMyFollowing().getCount() + "");
            beAttentionCountTv.setText(userStatisticInfo.getCountFollowMe().getCount() + "");
        }
    }

    public void bindData(UserInfo info) {
        this.mUserInfo = info;
        userInfoModifyBtn.setVisibility(View.VISIBLE);
        if (info.getRole() == Role.MATCHER) {
            matcherTagIv.setVisibility(View.VISIBLE);
            userDescTv.setText(info.getCompany() + " " + info.getOccupation());
        } else {
            matcherTagIv.setVisibility(View.GONE);
            userDescTv.setText(info.getMyMonologue());
        }

        userPhotoIv.init(info, false);
        userNameTv.setText(info.getMyNickname());
    }

    public void modifyBtnEnable(boolean isEnable) {
        if (isEnable) {
            userInfoModifyBtn.setVisibility(View.VISIBLE);
        } else {
            userInfoModifyBtn.setVisibility(View.GONE);
        }
    }
}
