package com.oneone.modules.profile;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.annotation.ToolbarResource;
import com.oneone.framework.ui.dialog.SheetDialog;
import com.oneone.framework.ui.dialog.SheetItem;
import com.oneone.framework.ui.utils.Res;
import com.oneone.modules.feedback.activity.ReportUserActivity;
import com.oneone.modules.profile.dialog.ShareDialog;
import com.oneone.modules.profile.fragment.AbstractProfileFragment;
import com.oneone.modules.profile.fragment.ProfileSinglesFragment;
import com.oneone.modules.profile.fragment.ProfileStoryFragment;
import com.oneone.modules.profile.fragment.ProfileTimelineFragment;
import com.oneone.modules.profile.view.AbstractProfileView;
import com.oneone.modules.profile.view.DumpingView4Single;
import com.oneone.modules.profile.view.HeaderView4Single;
import com.oneone.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Route(path = "/profile/info")
@ToolbarResource(layout = R.layout.toolbar_profile, navigationIcon = -1, title = R.string.empty_str, background = R.color.color_white)
@LayoutResource(R.layout.activity_profile)
public class Profile4SingleActivity extends AbstractProfileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightIconMenu(R.drawable.ic_profile_menu);
        setRightMostIconMenu(R.drawable.ic_profile_close);
    }

    @Override
    public void onRightIconMenuClick(View view) {
        super.onRightIconMenuClick(view);
        SheetDialog mDialog = new SheetDialog(mContext, R.array.profile_4single_activity_sheet_share_report, 1, new SheetDialog.OnSheetItemClickListener() {
            @Override
            public void onItemClick(SheetItem item, int id) {
                if (item.getId() == 0) {
                    new ShareDialog(mContext, getUserInfo()).show();
                } else {
                    ReportUserActivity.startActivity(mContext, getUserInfo());
                }
            }
        });
        mDialog.show();
    }


    @Override
    public void onRightMostIconMenuClick(View view) {
        super.onRightMostIconMenuClick(view);
        this.finish();
    }

    @Override
    public List<AbstractProfileFragment> getFragments() {
        List<AbstractProfileFragment> mFragments = new ArrayList<>();
        mFragments.add(new ProfileStoryFragment(getViewPager(), 0));
        mFragments.add(new ProfileTimelineFragment(getViewPager(), 1));
        mFragments.add(new ProfileSinglesFragment(getViewPager(), 2));
        return mFragments;
    }

    @Override
    public int getInitTabPosition(String tab) {
        switch (tab) {
            case ProfileStater.TAB_SINGLES:
                return 2;

            case ProfileStater.TAB_TIMELINE:
                return 1;

            case ProfileStater.TAB_STORY:
                return 0;
        }
        return 0;
    }

    public String[] getTitles() {
        return new String[]{Res.getString(R.string.str_profile_mystory),
                Res.getString(R.string.str_profile_timeline), Res.getString(R.string.str_profile_singles)};
    }

    @Override
    public AbstractProfileView getHeaderView() {
        return new HeaderView4Single(this, mPresenter);
    }

    @Override
    public AbstractProfileView getDumpingChildView() {
        return new DumpingView4Single(this);
    }

}
