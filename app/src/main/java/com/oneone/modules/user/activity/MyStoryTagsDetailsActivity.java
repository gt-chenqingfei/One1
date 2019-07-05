package com.oneone.modules.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.event.EventNextStep;
import com.oneone.event.EventProfileUpdateByRole;
import com.oneone.event.EventProfileUpdated;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.modules.mystory.contract.StoryContract;
import com.oneone.modules.mystory.presenter.StoryPresenter;
import com.oneone.modules.user.fragment.ProfileCharacterEditFrag;
import com.oneone.modules.user.fragment.ProfileExperienceEditFrag;
import com.oneone.modules.user.fragment.ProfileOccupationEditFrag;
import com.oneone.modules.user.fragment.ProfileSenseEditFrag;
import com.oneone.modules.user.fragment.ProfileSkillEditFrag;
import com.oneone.modules.user.fragment.ProfileSpouseEditFrag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author qingfei.chen
 * @since 2018/5/2.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@ToolbarResource(navigationIcon = R.drawable.ic_btn_back_light, title = R.string.empty_str, background = R.color.color_transparent)
@LayoutResource(R.layout.activity_mystory_tags_details)
public class MyStoryTagsDetailsActivity extends BaseActivity<StoryPresenter, StoryContract.View> implements StoryContract.View {

    public static void startActivity(Context context, String type) {
        Intent it = new Intent(context, MyStoryTagsDetailsActivity.class);
        it.putExtra(EXTRA_TYPE, type);
        it.putExtra(ProfileOccupationEditFrag.EXTRA_SHOW_PROGRESS, false);
        context.startActivity(it);
    }

    public static final String TYPE_OCCUPATION = "TYPE_OCCUPATION";
    public static final String TYPE_SKILL = "TYPE_SKILL";
    public static final String TYPE_CHARACTER = "TYPE_CHARACTER";
    public static final String TYPE_EXPERIENCE = "TYPE_EXPERIENCE";
    public static final String TYPE_SENSE = "TYPE_SENSE";
    public static final String TYPE_SPOUSE = "TYPE_SPOUSE";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tvMenu = setRightTextMenu(R.string.str_menu_save);
        tvMenu.setTextColor(getResources().getColor(R.color.color_white));

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getFragmentByType(type);
        if (fragment != null) {
            manager.beginTransaction().add(R.id.frag_container, fragment).commit();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        type = intent.getStringExtra(EXTRA_TYPE);
    }

    @Override
    public StoryPresenter onCreatePresenter() {
        return new StoryPresenter();
    }

    @Override
    public void onRightTextMenuClick(View view) {
        super.onRightTextMenuClick(view);
        EventBus.getDefault().post(new EventNextStep());
    }

    private Fragment getFragmentByType(String type) {
        Fragment fragment = null;
        switch (type) {

            case TYPE_OCCUPATION:
                fragment = new ProfileOccupationEditFrag();
                break;
            case TYPE_SKILL:
                fragment = new ProfileSkillEditFrag();
                break;
            case TYPE_CHARACTER:
                fragment = new ProfileCharacterEditFrag();
                break;
            case TYPE_EXPERIENCE:
                fragment = new ProfileExperienceEditFrag();
                break;
            case TYPE_SENSE:
                fragment = new ProfileSenseEditFrag();
                break;
            case TYPE_SPOUSE:
                fragment = new ProfileSpouseEditFrag();
                break;
        }

        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventProfileUpdate(EventProfileUpdateByRole event) {
        mPresenter.updateUserInfo(new StoryContract.OnUserInfoUpdateListener() {
            @Override
            public void onUserInfoUpdate() {
                EventBus.getDefault().post(new EventProfileUpdated());
                MyStoryTagsDetailsActivity.this.finish();
            }
        }, event.getUserInfo());
    }

}


