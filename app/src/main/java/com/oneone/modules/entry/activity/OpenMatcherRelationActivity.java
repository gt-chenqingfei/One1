package com.oneone.modules.entry.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventFinishActivity;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.entry.contract.OpenRelationContract;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.user.fragment.ProfileAvatarEditFrag;
import com.oneone.modules.user.fragment.ProfileNameEditFrag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author qingfei.chen
 * @since 2018/4/25.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, title = R.string.empty_str, background = R.color.transparent)
@LayoutResource(R.layout.activity_open_single_relation)
public class OpenMatcherRelationActivity extends BaseActivity<OpenRelationPresenter, OpenRelationContract.View> implements
        OpenRelationContract.View, OpenRelationContract.OnUserInfoUpdateListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OpenMatcherRelationActivity.class));
    }

    public static final int STEP_EDIT_NICK_NAME = 0;
    public static final int STEP_EDIT_AVATAR = 1;

    private int currentStep = STEP_EDIT_NICK_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar.keyboardEnable(true).init();
        setTitleString(getString(R.string.str_open_matchr_title));
        getTitleView().setTextColor(getResources().getColor(R.color.color_white));
        getRightTextMenu().setVisibility(View.GONE);
        fragmentShowOrAdd();
        EventBus.getDefault().register(this);
    }

    @Override
    public OpenRelationPresenter onCreatePresenter() {
        return new OpenRelationPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishActivity(EventFinishActivity event) {
        finish();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);

        EventBus.getDefault().post(new EventNextStep());
    }

    @Override
    public boolean onNavigationClick(View view) {
        if (currentStep <= 0) {
            return super.onNavigationClick(view);
        }

        performPreStep();
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (currentStep <= 0) {
                return super.dispatchKeyEvent(event);
            }

            performPreStep();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onUserInfoUpdate() {
        if (currentStep == STEP_EDIT_AVATAR) {
            mPresenter.openRole(Role.MATCHER, new OpenRelationContract.OnOpenRoleListener() {
                @Override
                public void onOpenRole(boolean isOk) {
                    AppInitializer.getInstance().
                            startMainAndLoadPreData(OpenMatcherRelationActivity.this);
                }
            });
            return;
        }
        performNextStep();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProfileUpdateEvent(EventProfileUpdateByRole event) {
        mPresenter.updateUserByRole(Role.MATCHER, event.getUserInfo(), this);
    }

    private void performNextStep() {
        fragmentHide();
        currentStep++;
        if (currentStep == STEP_EDIT_AVATAR) {
            currentStep = STEP_EDIT_AVATAR;
        }
        fragmentShowOrAdd();
    }

    private void performPreStep() {
        fragmentHide();
        currentStep--;
        if (currentStep == STEP_EDIT_NICK_NAME) {
            currentStep = STEP_EDIT_NICK_NAME;
        }
        fragmentShowOrAdd();
    }

    private void fragmentShowOrAdd() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(currentStep + "");
        if (fragment == null) {
            fragment = getFragmentByStep(currentStep);
            manager.beginTransaction().add(R.id.activity_profile_set_guide_frag_container, fragment, currentStep + "").show(fragment).commit();
        } else {
            manager.beginTransaction().show(fragment).commit();
        }
    }

    private void fragmentHide() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(currentStep + "");
        if (fragment != null) {
            manager.beginTransaction().hide(fragment).commit();
        }
    }

    private Fragment getFragmentByStep(int step) {
        Fragment fragment = null;
        switch (step) {
            case STEP_EDIT_NICK_NAME:
                fragment = new ProfileNameEditFrag();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMatcher", true);
                fragment.setArguments(bundle);
                break;

            case STEP_EDIT_AVATAR:
                fragment = new ProfileAvatarEditFrag();
                break;
        }

        return fragment;
    }

}
