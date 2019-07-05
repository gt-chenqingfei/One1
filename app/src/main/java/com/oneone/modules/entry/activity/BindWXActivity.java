package com.oneone.modules.entry.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.event.EventFinishActivity;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.Res;
import com.oneone.modules.entry.contract.AccountContract;
import com.oneone.modules.entry.presenter.AccountPresenter;
import com.oneone.support.share.Callback;
import com.oneone.support.share.PlatformUtil;
import com.oneone.support.share.ShareBase;
import com.oneone.support.share.ShareParams;
import com.oneone.support.share.Wechat;
import com.oneone.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;

@ToolbarResource(title = R.string.str_wx_auth_page_wx_auth, navigationIcon = -1)
@LayoutResource(R.layout.activity_wx_auth_page)
public class BindWXActivity extends BaseActivity<AccountPresenter, AccountContract.View>
        implements OnClickListener, AccountContract.View, Callback {

    @BindView(R.id.wx_auth_btn_layout)
    View mTvWxAuthBtn;

    @BindView(R.id.tv_wx_auth_btn_layout)
    TextView mTvWxAuth;

    @BindView(R.id.wx_login_origin)
    View mTvWXOriginLogin;

    @BindView(R.id.wx_auth_tag_tv_1)
    TextView mTvAuthTag;

    @BindView(R.id.wx_auth_tag_tv_2)
    TextView mTvAuthTag1;

    @BindView(R.id.wx_auth_phone_bg_iv)
    ImageView mIvTip;

    String mPlatform;
    String mPlatformId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvWxAuthBtn.setOnClickListener(this);
        mTvWXOriginLogin.setOnClickListener(this);
        EventBus.getDefault().register(this);
        refreshUi(false);
        setRightTextMenu(R.string.str_set_single_flow_page_jump_to_next);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        goUserRoleSelect();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public AccountPresenter onCreatePresenter() {
        return new AccountPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_auth_btn_layout:
                getWXAuth();
                break;

            case R.id.wx_login_origin:
                mPresenter.loginByThirdPart(mPlatform, mPlatformId);
                break;
        }


    }

    @Override
    public void validCodeEditViewEnable() {

    }

    @Override
    public void onValidCodeGet(int status) {

    }

    @Override
    public void goHome() {
        AppInitializer.getInstance().startMainAndLoadPreData(this);
    }

    @Override
    public void goBindPhone(String platform, String platformId) {
        BindMobileActivity.startActivity(this, platform, platformId);
    }

    @Override
    public void goUserRoleSelect() {
        UserRoleSelectedActivity.startActivity(this);
    }

    @Override
    public void goBindWXIfInstall() {
        if (!PlatformUtil.isWechatValid(this)) {
            ToastUtil.show(this,
                    getString(R.string.str_app_notice_not_install_wechat));
            return;
        }
        Intent it = new Intent(this, BindWXActivity.class);
        startActivity(it);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishActivity(EventFinishActivity event) {
        finish();
    }


    @Override
    public void onComplete(ShareParams shareParams, Platform platform, int i, HashMap<String, Object> hashMap) {

        if (platform.getName().equals(cn.sharesdk.wechat.friends.Wechat.NAME)) {
            mPresenter.bindThird(platform.getName().toLowerCase(), platform.getDb().getUserId());
        }
    }

    @Override
    public void onError(ShareParams shareParams, Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(ShareParams shareParams, Platform platform, int i) {

    }

    private void getWXAuth() {
        ShareBase shareBase = new Wechat(this, this);
        boolean ret = shareBase.getAuthorized();
        if (!ret) {
            showError(Res.getString(R.string.str_app_notice_not_install_wechat));
        }
    }

    @Override
    public void alreadyBindWX(String platform, final String platformId) {
        this.mPlatform = platform;
        this.mPlatformId = platformId;

        refreshUi(true);
    }

    private void refreshUi(boolean isBindWX) {
        if (isBindWX) {
            mTvWxAuth.setText(R.string.str_wx_auth_page_wx_auth1);
            mTvAuthTag1.setText(R.string.str_wx_bind_phone_tip);
            mTvAuthTag.setVisibility(View.GONE);
            mTvWXOriginLogin.setVisibility(View.VISIBLE);
            mIvTip.setImageResource(R.drawable.ic_bind_wechat_tip);
        } else {
            mTvWxAuth.setText(R.string.str_wx_auth_page_wx_auth);
            mTvAuthTag1.setText(R.string.str_wx_auth_page_wx_auth_explain);
            mTvAuthTag.setVisibility(View.VISIBLE);
            mTvWXOriginLogin.setVisibility(View.GONE);
            mIvTip.setImageResource(R.drawable.wx_auth_phone_bg);
        }
    }


}
