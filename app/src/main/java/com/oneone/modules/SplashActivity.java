package com.oneone.modules;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventFinishActivity;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.entry.activity.AccountExceptionActivity;
import com.oneone.modules.entry.activity.BindWXActivity;
import com.oneone.modules.entry.activity.LoginActivity;
import com.oneone.modules.entry.activity.UserRoleSelectedActivity;
import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.modules.entry.contract.SplashContract;
import com.oneone.modules.entry.presenter.SplashPresenter;
import com.oneone.modules.main.MainActivity;
import com.oneone.modules.user.HereUser;
import com.oneone.schema.SchemaUtil;
import com.oneone.support.share.PlatformUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/3/31.
 */
@LayoutResource(R.layout.activity_splash)
public class SplashActivity extends BaseActivity<SplashPresenter, SplashContract.View>
        implements SplashContract.View, SplashContract.OnLoginInfoListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        HereUser user = HereUser.getInstance();
        if (user == null || TextUtils.isEmpty(user.getToken())) {
            goLogin();
            return;
        }
        if (user.getUserInfo() != null
                && user.getUserInfo().getRole() != Role.UNKNOWN) {
            goHomeIfPreDataLoaded();
            return;
        }

        mPresenter.getLoginInfo(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoginInfo(LoginInfo loginInfo) {
        switch (loginInfo.getAccountRole()) {
            case Role.UNKNOWN:
                goRoleSelect();
                break;
            default:
                goHomeIfPreDataLoaded();
                break;
        }
    }

    @Override
    public SplashPresenter onCreatePresenter() {
        return new SplashPresenter();
    }

    @Override
    public void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        boolean booleanExtra = getIntent().getBooleanExtra("EXTRA_KICK_OUT", false);
        intent.putExtra("EXTRA_KICK_OUT", booleanExtra);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void goRoleSelect() {
        UserRoleSelectedActivity.startActivity(this);
        this.finish();
    }

    public void goBindWXIfInstall() {
        if (!PlatformUtil.isWechatValid(this)) {
            goRoleSelect();
            return;
        }
        Intent it = new Intent(this, BindWXActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void goAccountException() {
        Intent intent = new Intent(SplashActivity.this,
                AccountExceptionActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void goHomeIfPreDataLoaded() {
        AppInitializer.getInstance().startMainAndLoadPreData(this,
                new AppInitializer.OnLoadPreDataListener() {
                    @Override
                    public void onLoadPreData(boolean isAppLaunched) {
                        SchemaUtil.doFilter(getIntent(), SplashActivity.this);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFinishActivity(EventFinishActivity event) {
        finish();
    }


}
