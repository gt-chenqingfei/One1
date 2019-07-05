package com.oneone.modules.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.utils.Res;
import com.oneone.modules.profile.dialog.ShareDialog;
import com.oneone.modules.profile.fragment.AbstractProfileFragment;
import com.oneone.modules.profile.fragment.ProfileSinglesFragment;
import com.oneone.modules.profile.fragment.ProfileStoryFragment;
import com.oneone.modules.profile.fragment.ProfileTimelineFragment;
import com.oneone.modules.profile.view.AbstractProfileView;
import com.oneone.modules.profile.view.DumpingView4Matcher;
import com.oneone.modules.profile.view.HeaderView4Matcher;
import com.oneone.modules.user.HereUser;
import com.oneone.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@Route(path = "/profile/info")
@ToolbarResource(layout = R.layout.toolbar_profile, navigationIcon = -1, title = R.string.empty_str, background = R.color.color_6667FD)
@LayoutResource(R.layout.activity_profile)
public class Profile4MatcherActivity extends AbstractProfileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightMostIconMenu(R.drawable.ic_matcher_profile_close);
        if (userId.equals(HereUser.getInstance().getUserId())) {
            setNavigation(R.drawable.ic_white_share);
        }
    }

    @Override
    public void onRightMostIconMenuClick(View view) {
        super.onRightMostIconMenuClick(view);
        this.finish();
    }

    @Override
    public boolean onNavigationClick(View view) {
        // new ShareUtil(this, getUserInfo()).show();
        ShareDialog mShareDialog = new ShareDialog(mContext,  getUserInfo());
        mShareDialog.show();
        return true;
    }

    @Override
    public List<AbstractProfileFragment> getFragments() {
        List<AbstractProfileFragment> mFragments = new ArrayList<>();
        mFragments.add(new ProfileSinglesFragment(getViewPager(), 0));
        mFragments.add(new ProfileTimelineFragment(getViewPager(), 1));
        return mFragments;
    }

    @Override
    public int getInitTabPosition(String tab) {
        switch (tab) {
            case ProfileStater.TAB_SINGLES:
                return 0;
            case ProfileStater.TAB_TIMELINE:
                return 1;
        }
        return 0;
    }

    public String[] getTitles() {
        return new String[]{Res.getString(R.string.str_profile_singles), Res.getString(R.string.str_profile_timeline)};
    }


    @Override
    public AbstractProfileView getHeaderView() {
        return new HeaderView4Matcher(this);
    }

    @Override
    public AbstractProfileView getDumpingChildView() {
        return new DumpingView4Matcher(this);
    }

}
