package com.oneone.modules.timeline.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.profile.ProfileStater;
import com.oneone.modules.timeline.contract.TimeLineContract;
import com.oneone.modules.user.bean.UserInfoBase;
import com.oneone.modules.user.util.HereUserUtil;
import com.oneone.utils.ImageHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @version V1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/5/30.
 */
@LayoutResource(R.layout.view_timeline_new_single)
public class TimeLineNewSingleView extends AbstractTimeLineView<UserInfoBase> implements View.OnClickListener {
    @BindView(R.id.view_timeline_new_single_iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.view_timeline_new_single_tv_display)
    TextView mTvDisplay;
    @BindView(R.id.view_timeline_new_single_tv_display_first)
    TextView mTvDisplayFirst;

    @BindView(R.id.view_timeline_new_single_tv_summary)
    TextView mTvSummary;

    private UserInfoBase mUserInfoBase;

    public TimeLineNewSingleView(Context context) {
        super(context);
    }

    public TimeLineNewSingleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void bind(UserInfoBase userInfoBase, TimeLineContract.Presenter presenter, int position) {
        this.mUserInfoBase = userInfoBase;
        if (userInfoBase == null) {
            return;
        }
        String nameAndSummary = userInfoBase.getMyNickname() + " " + HereUserUtil.getSummary1(userInfoBase);
        mTvDisplay.setText(spannableUserName(userInfoBase, nameAndSummary, R.color.black, R.color.color_7E94BB));

        String nameAndHint = userInfoBase.getMyNickname() + " " + getResources().getString(R.string.str_add_to_your_singles);
        mTvDisplayFirst.setText(spannableUserName(userInfoBase, nameAndHint, R.color.color_796CF0, R.color.color_black));

        if (userInfoBase.getRole() == Role.SINGLE) {
            ImageHelper.displayAvatar(getContext(), mIvAvatar, userInfoBase.getMyAvatar(), R.drawable.default_avatar_single);
        } else if (userInfoBase.getRole() == Role.MATCHER) {
            ImageHelper.displayAvatar(getContext(), mIvAvatar, userInfoBase.getMyAvatar(), R.drawable.default_avatar_matcher);
        } else {
            ImageHelper.displayAvatar(getContext(), mIvAvatar, userInfoBase.getMyAvatar(), R.drawable.default_avatar_oneone);
        }
    }

    @OnClick(R.id.view_timeline_new_single_ll_info)
    public void onClick(View view) {
        ProfileStater.startWithUserInfo(getContext(), this.mUserInfoBase);
    }

    public SpannableString spannableUserName(UserInfoBase userInfoBase, String nameAndHint, int nameColor, int summaryColor) {
        SpannableString spannedString = new SpannableString(nameAndHint);
        spannedString.setSpan(new ForegroundColorSpan(getResources().getColor(nameColor)),
                0, userInfoBase.getMyNickname().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannedString.setSpan(new ForegroundColorSpan(getResources().getColor(summaryColor)),
                userInfoBase.getMyNickname().length(), nameAndHint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannedString;
    }
}
