package com.oneone.modules.matcher.relations.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.Res;
import com.oneone.modules.matcher.relations.contract.SinglesContract;
import com.oneone.modules.matcher.relations.presenter.SinglesPresenter;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.modules.user.HereUser;
import com.oneone.support.share.Callback;
import com.oneone.support.share.ShareBase;
import com.oneone.support.share.ShareBox;
import com.oneone.support.share.ShareParams;
import com.oneone.support.share.Wechat;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * @author qingfei.chen
 * @since 2018/4/23.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, title = R.string.empty_str, background = R.color.color_796CF0)
@LayoutResource(R.layout.activity_matcher_invite)
public class MatcherInviteActivity extends BaseActivity<SinglesPresenter, SinglesContract.View>
        implements Callback, SinglesContract.OnBindWechatListener, SinglesContract.View, View.OnClickListener{

    @BindView(R.id.share_invite_matcher)
    ShareBox shareBox;
    @BindView(R.id.activity_matcher_invite_friend_ll)
    LinearLayout inviteFriend;
    @BindView(R.id.activity_matcher_invite_bind_wechat_ll)
    LinearLayout inviteBindWechat;
    @BindView(R.id.invite_matcher_qa_layout)
    RelativeLayout inviteMatcherQaLayout;
    @BindView(R.id.invite_matcher_what_is_matcher_layout)
    RelativeLayout inviteMatcherWhatIsMatherLayout;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MatcherInviteActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareBox.setGroupID(ShareInfo.SHARE_INVITE_MATCHER);
        inviteMatcherQaLayout.setOnClickListener(this);
        inviteMatcherWhatIsMatherLayout.setOnClickListener(this);
        refreshUi();
    }

    @OnClick(R.id.activity_matcher_invite_bind_btn_layout)
    public void onBindWechatClick(View view) {
        getWXAuth();
    }

    private void getWXAuth() {
        ShareBase shareBase = new Wechat(this, this);
        boolean ret = shareBase.getAuthorized();
        if (!ret) {
            showError(Res.getString(R.string.str_app_notice_not_install_wechat));
        }
    }

    @Override
    public void onBindWeChat() {
        refreshUi();
    }

    private void refreshUi() {
        if (HereUser.getInstance().getLoginInfo().isAlreadyBindWechat()) {
            inviteFriend.setVisibility(View.VISIBLE);
            inviteBindWechat.setVisibility(View.GONE);
        } else {
            inviteFriend.setVisibility(View.GONE);
            inviteBindWechat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public SinglesPresenter onCreatePresenter() {
        return new SinglesPresenter();
    }

    @Override
    public void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(cn.sharesdk.wechat.friends.Wechat.NAME)) {
            mPresenter.bindWeChat(platform.getName().toLowerCase(), platform.getDb().getUserId(), this);
        }
    }

    @Override
    public void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable) {
    }

    @Override
    public void onCancel(ShareParams shareParams, Platform platform, int i) {
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
        }
    }
}
