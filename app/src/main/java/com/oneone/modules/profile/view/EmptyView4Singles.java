package com.oneone.modules.profile.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.matcher.relations.ui.SinglesInviteActivity;
import com.oneone.modules.user.HereUser;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author qingfei.chen
 * @since 2018/6/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.empty_profile_singles)
public class EmptyView4Singles extends BaseView {

    @BindView(R.id.empty_profile_singles_btn_invite)
    TextView mTvBtnInvite;
    private String userId;
    public EmptyView4Singles(Context context,String userId) {
        super(context);
        this.userId = userId;
        if(HereUser.getUserId().equals(userId)){
            mTvBtnInvite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated() {

    }

    @OnClick(R.id.empty_profile_singles_btn_invite)
    public void onInviteClick(View view) {
        SinglesInviteActivity.startActivity(getContext());
    }

}
