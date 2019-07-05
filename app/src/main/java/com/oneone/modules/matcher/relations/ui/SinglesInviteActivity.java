package com.oneone.modules.matcher.relations.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.Res;
import com.oneone.framework.ui.utils.ScreenUtil;
import com.oneone.modules.matcher.relations.contract.SinglesContract;
import com.oneone.modules.matcher.relations.presenter.SinglesPresenter;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.modules.user.HereUser;
import com.oneone.support.share.Callback;
import com.oneone.support.share.ShareBase;
import com.oneone.support.share.ShareBox;
import com.oneone.support.share.ShareParams;
import com.oneone.support.share.ShareParamsUtil;
import com.oneone.support.share.Wechat;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * @author qingfei.chen
 * @since 2018/4/23.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@ToolbarResource(title = R.string.empty_str, background = R.color.color_796CF0, navigationIcon = R.drawable.ic_btn_back_light)
@LayoutResource(R.layout.activity_singles_invite)
public class SinglesInviteActivity extends BaseActivity<SinglesPresenter,SinglesContract.View>
implements Callback,SinglesContract.OnBindWechatListener, SinglesContract.View{
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SinglesInviteActivity.class));
    }

    @BindView(R.id.activity_singles_invite_ll)
    ViewGroup mVgInvite;
    @BindView(R.id.share_invite_single)
    ShareBox shareBox;
    @BindView(R.id.activity_singles_invite_bind_wechat_ll)
    ViewGroup mVgBindWechat;


    @Override
    public SinglesPresenter onCreatePresenter() {
        return new SinglesPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshUi();
        shareBox.setGroupID(ShareInfo.SHARE_INVITE_SINGLE);
    }


    @OnClick(R.id.activity_singles_invite_bind_btn_layout)
    public void onBindWechatClick(View view){
        getWXAuth();
    }

    @Override
    public void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap) {

        if (platform.getName().equals(cn.sharesdk.wechat.friends.Wechat.NAME)) {
            mPresenter.bindWeChat(platform.getName().toLowerCase(), platform.getDb().getUserId(),this);
        }

    }

    @Override
    public void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(ShareParams shareParams, Platform platform, int i) {

    }

    @Override
    public void onBindWeChat() {
        refreshUi();
    }

    private void getWXAuth() {
        ShareBase shareBase = new Wechat(this, this);
        boolean ret = shareBase.getAuthorized();
        if (!ret) {
            showError(Res.getString(R.string.str_app_notice_not_install_wechat));
        }
    }

    private void refreshUi(){
        if(HereUser.getInstance().getLoginInfo().isAlreadyBindWechat()){
            mVgInvite.setVisibility(View.VISIBLE);
            mVgBindWechat.setVisibility(View.GONE);
        }
        else{
            mVgInvite.setVisibility(View.GONE);
            mVgBindWechat.setVisibility(View.VISIBLE);
        }
    }

}
