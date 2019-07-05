package com.oneone.modules.profile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.widget.AvatarImageView;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_profile_dumping_view_4_matcher)
public class DumpingView4Matcher extends AbstractProfileView {
    @BindView(R.id.layout_profile_header_view_4_matcher_iv_avatar)
    AvatarImageView mIvAvatar;
    @BindView(R.id.layout_profile_header_view_4_matcher_tv_name)
    TextView mTvName;
    @BindView(R.id.layout_profile_header_view_4_matcher_tv_occupation)
    TextView mTvOccupation;

    public DumpingView4Matcher(Context context) {
        super(context);
    }

    @Override
    public void bindData(UserInfo userInfo, ProfilePresenter presenter) {
        if (userInfo == null) {
            return;
        }
        mIvAvatar.init(userInfo, true);
        mTvName.setText(userInfo.getMyNickname());
        mTvOccupation.setText(userInfo.getCompany() + " " + userInfo.getOccupation());
    }
}
