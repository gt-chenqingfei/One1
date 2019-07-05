package com.oneone.modules.entry.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.oneone.AppInitializer;
import com.oneone.BaseActivity;
import com.oneone.Constants;
import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventFinishActivity;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.framework.android.utils.ActivityUtils;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.entry.contract.OpenRelationContract;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.fragment.ProfileAvatarAndStoryEditFrag;
import com.oneone.modules.user.fragment.ProfileBirthdayEditFrag;
import com.oneone.modules.user.fragment.ProfileCharacterEditFrag;
import com.oneone.modules.user.fragment.ProfileCityEditFrag;
import com.oneone.modules.user.fragment.ProfileExperienceEditFrag;
import com.oneone.modules.user.fragment.ProfileGenderEditFrag;
import com.oneone.modules.user.fragment.ProfileNameEditFrag;
import com.oneone.modules.user.fragment.ProfileOccupationEditFrag;
import com.oneone.modules.user.fragment.ProfileOpenSingleFrag;
import com.oneone.modules.user.fragment.ProfilePreviewFrag;
import com.oneone.modules.user.fragment.ProfileSenseEditFrag;
import com.oneone.modules.user.fragment.ProfileSkillEditFrag;
import com.oneone.modules.user.fragment.ProfileSpouseEditFrag;
import com.oneone.modules.entry.presenter.OpenRelationPresenter;
import com.oneone.modules.user.UserSP;

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
public class OpenSingleRelationActivity extends BaseActivity<OpenRelationPresenter,
        OpenRelationContract.View> implements OpenRelationContract.View, OpenRelationContract.OnUserInfoUpdateListener {
    public static String EXTRA_ADD_ROLE = "ADD_ROLE";

    public static void startActivity(Context context, boolean addRole) {
        Intent intent = new Intent(context, OpenSingleRelationActivity.class);
        intent.putExtra(EXTRA_ADD_ROLE, addRole);
        context.startActivity(intent);
    }

    public static final int STEP_GENDER = 0;
    public static final int STEP_NICK_NAME = 1;
    public static final int STEP_CITY = 2;
    public static final int STEP_BIRTHDAY = 3;
    public static final int STEP_OCCUPATION = 4;
    public static final int STEP_SKILL = 5;
    public static final int STEP_CHARACTER = 6;
    public static final int STEP_EXPERIENCE = 7;
    public static final int STEP_SENSE = 8;
    public static final int STEP_SPOUSE = 9;
    public static final int STEP_AVATAR = 10;
    public static final int STEP_PREVIEW = 11;
    public static final int STEP_OPEN_SINGLE = 12;

    private int currentStep = STEP_GENDER;
    boolean addRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar.keyboardEnable(true).init();
        setRightTextMenu(getString(R.string.str_next)).setTextColor(getResources().getColor(R.color.color_white));
        currentStep = getCurrentStep(OpenRelationPresenter.getTempUserInfo());
        addRole = getIntent().getBooleanExtra(EXTRA_ADD_ROLE, false);
        if (addRole) {
            mPresenter.addRole(Role.SINGLE, null);
        }
        performStep();
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
            } else if (currentStep == STEP_PREVIEW) {
                return true;
            }

            performPreStep();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onUserInfoUpdate() {
        performNextStep();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProfileUpdateEvent(EventProfileUpdateByRole event) {
        switch (currentStep) {
            case STEP_PREVIEW:
                performNextStep();
                break;

            case STEP_OPEN_SINGLE:
                openRoleOrChooseRole();
                break;

            default:
                mPresenter.updateUserByRole(Role.SINGLE, event.getUserInfo(), this);
                break;
        }
    }

    private void openRoleOrChooseRole() {
        mPresenter.openRole(Role.SINGLE, new OpenRelationContract.OnOpenRoleListener() {
            @Override
            public void onOpenRole(boolean isOk) {
                if (isOk) {
                    if (addRole) {
                        ActivityUtils.finishAllActivities();
                    }
                    loading("");
                    AppInitializer.getInstance().startMainAndResetPreData(OpenSingleRelationActivity.this, new AppInitializer.OnLoadPreDataListener() {
                        @Override
                        public void onLoadPreData(boolean isAppLaunched) {
                            loadingDismiss();
                            OpenSingleRelationActivity.this.finish();
                        }
                    });
                }
            }
        });
    }

    private void performNextStep() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(currentStep + "");
        if (fragment != null) {
            manager.beginTransaction().hide(fragment).commit();
        }

        currentStep++;
        if (currentStep == STEP_OPEN_SINGLE) {
            currentStep = STEP_OPEN_SINGLE;
        }
        performStep();
    }

    private void performPreStep() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(currentStep + "");
        if (fragment != null) {
            manager.beginTransaction().hide(fragment).commit();
        }

        currentStep--;
        if (currentStep == STEP_GENDER) {
            currentStep = STEP_GENDER;
        }
        performStep();
    }

    private void performStep() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(currentStep + "");
        if (fragment == null) {
            fragment = getFragmentByStep(currentStep);
            manager.beginTransaction().add(R.id.activity_profile_set_guide_frag_container, fragment, currentStep + "").show(fragment).commit();
        } else {
            manager.beginTransaction().show(fragment).commit();
        }

        refreshMenuTextByStep();
    }

    private void refreshMenuTextByStep() {
        switch (currentStep) {
            case STEP_GENDER:
            case STEP_NICK_NAME:
            case STEP_CITY:
            case STEP_BIRTHDAY:
                getRightTextMenu().setVisibility(View.GONE);
                setTitleString(getString(R.string.str_set_single_flow_page_createing_single_tv));
                getTitleView().setTextColor(getResources().getColor(R.color.color_white));
                break;
            case STEP_OCCUPATION:
            case STEP_SKILL:
            case STEP_CHARACTER:
            case STEP_EXPERIENCE:
            case STEP_SENSE:
            case STEP_AVATAR:
                getRightTextMenu().setText(R.string.str_set_single_flow_page_next_step);
                getRightTextMenu().setVisibility(View.VISIBLE);
                setTitleString("");
                break;

            case STEP_SPOUSE:
                getRightTextMenu().setText(R.string.str_set_single_flow_page_next_this);
                getRightTextMenu().setVisibility(View.VISIBLE);
                setTitleString("");
                break;

            case STEP_PREVIEW:
                getRightTextMenu().setText(R.string.str_set_single_flow_page_next_step);
                getRightTextMenu().setVisibility(View.VISIBLE);
                getTitleView().setTextColor(getResources().getColor(R.color.color_white));
                setTitleString(getString(R.string.str_set_single_flow_page_preview));
                getToolbar().setNavigationIcon(null);
                break;
            case STEP_OPEN_SINGLE:
                getRightTextMenu().setVisibility(View.VISIBLE);
                getTitleView().setTextColor(getResources().getColor(R.color.color_white));
                getRightTextMenu().setText(R.string.str_set_single_flow_page_jump_to_next);
                setTitleString("");
                break;
        }
    }

    private Fragment getFragmentByStep(int step) {
        Fragment fragment = null;
        switch (step) {
            case STEP_GENDER:
                fragment = new ProfileGenderEditFrag();
                break;

            case STEP_NICK_NAME:
                fragment = new ProfileNameEditFrag();
                break;

            case STEP_CITY:
                fragment = new ProfileCityEditFrag();
                break;

            case STEP_BIRTHDAY:
                fragment = new ProfileBirthdayEditFrag();
                break;

            case STEP_OCCUPATION:
                fragment = new ProfileOccupationEditFrag();
                break;

            case STEP_SKILL:
                fragment = new ProfileSkillEditFrag();
                break;

            case STEP_CHARACTER:
                fragment = new ProfileCharacterEditFrag();
                break;

            case STEP_EXPERIENCE:
                fragment = new ProfileExperienceEditFrag();
                break;

            case STEP_SENSE:
                fragment = new ProfileSenseEditFrag();
                break;

            case STEP_SPOUSE:
                fragment = new ProfileSpouseEditFrag();
                break;

            case STEP_AVATAR:
                fragment = new ProfileAvatarAndStoryEditFrag();
                break;

            case STEP_PREVIEW:
                fragment = new ProfilePreviewFrag();
                break;

            case STEP_OPEN_SINGLE:
                fragment = new ProfileOpenSingleFrag();
                break;
        }

        return fragment;
    }

    private int getCurrentStep(UserProfileUpdateBean info) {
        if (info == null) {
            return STEP_GENDER;
        }
        String storyPreviewBean = UserSP.getString(this, Constants.PREF.PREF_STORY_PREVIEW_BEAN, "");
        String avatar = info.getAvatar();
        if (!TextUtils.isEmpty(storyPreviewBean) && !TextUtils.isEmpty(avatar)) {
            return STEP_PREVIEW;
        }

        if (info.getSpousePrefsTags() != null && info.getSpousePrefsTags().getTagCount() > 0) {
            return STEP_AVATAR;
        } else if (info.getSenseOfWorthTags() != null && info.getSenseOfWorthTags().getTagCount() > 0) {
            return STEP_SPOUSE;
        } else if (info.getExperienceTags() != null && info.getExperienceTags().getTagCount() > 0) {
            return STEP_SENSE;
        } else if (info.getCharacterTags() != null && info.getCharacterTags().getTagCount() > 0) {
            return STEP_EXPERIENCE;
        } else if (info.getSkillTags() != null && info.getSkillTags().getTagCount() > 0) {
            return STEP_CHARACTER;
        } else if (info.getOccupationTags() != null && info.getOccupationTags().getTagCount() > 0) {
            return STEP_SKILL;
        } else if (info.getBirthdate() != null && !info.getBirthdate().equals("")) {
            return STEP_OCCUPATION;
        } else if (info.getCity() != null && !info.getCity().equals("")) {
            return STEP_BIRTHDAY;
        } else if (info.getNickname() != null && !info.getNickname().equals("")) {
            return STEP_CITY;
        } else if (info.getSex() != null) {
            return STEP_NICK_NAME;
        }

        return STEP_GENDER;
    }


}
