package com.oneone.modules.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.support.share.ShareBox;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.activity_open_one_one)
public class ProfileOpenSingleFrag extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.invite_matcher_open_oneone_btn_tv)
    TextView inviteMatcherOpenOneOneBtnTv;
    @BindView(R.id.invite_matcher_qa_layout)
    RelativeLayout inviteMatcherQaLayout;
    @BindView(R.id.invite_matcher_what_is_matcher_layout)
    RelativeLayout inviteMatcherWhatIsMatherLayout;
    @BindView(R.id.invite_matcher_invite_share_box)
    ShareBox shareBox;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inviteMatcherOpenOneOneBtnTv.setOnClickListener(this);
        inviteMatcherQaLayout.setOnClickListener(this);
        inviteMatcherWhatIsMatherLayout.setOnClickListener(this);
        EventBus.getDefault().register(this);
        shareBox.setGroupID(ShareInfo.SHARE_INVITE_MATCHER);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_matcher_qa_layout:
                inviteMatcherWhatIsMatherLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.invite_matcher_what_is_matcher_layout:
                inviteMatcherWhatIsMatherLayout.setVisibility(View.GONE);
                break;

            case R.id.invite_matcher_open_oneone_btn_tv:
                performOpenOneOne();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNextStepEvent(EventNextStep event) {
        performOpenOneOne();
    }

    private void performOpenOneOne() {
        EventBus.getDefault().post(new EventProfileUpdateByRole(null));
    }
}
