package com.oneone.modules.profile.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.matcher.relations.ui.SinglesInviteActivity;
import com.oneone.modules.timeline.activity.TimeLinePostActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.utils.ImageHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/6/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.empty_profile_timeline)
public class EmptyView4Timeline extends BaseView {

    @BindView(R.id.empty_profile_timeline_iv_tip)
    ImageView ivEmptyView;
    @BindView(R.id.empty_profile_timeline_tv_notimeline)
    TextView tvEmptyView;
    @BindView(R.id.empty_profile_timeline_btn_record)
    TextView tvRecord;

    public EmptyView4Timeline(Context context) {
        super(context);
    }

    @Override
    public void onViewCreated() {
        //  Single 和 Matcher 除了页面下面有加号按钮不同显示，其余都一样
        ImageHelper.displayGif(R.drawable.other_account_exception_gif, ivEmptyView);
        tvEmptyView.setText(mContext.getResources().getString(R.string.str_empty_timeline_tip_matcher));
        tvRecord.setVisibility(View.GONE);
//        if (HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER) {
//        } else {
//            ImageHelper.displayGif(R.drawable.take_pic_gif, ivEmptyView);
//        }
    }

    @OnClick(R.id.empty_profile_timeline_btn_record)
    public void onInviteClick(View view) {
        TimeLinePostActivity.startPostImageTextActivity(view.getContext());
    }
}
