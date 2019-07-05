package com.oneone.modules.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.oneone.R;
import com.oneone.api.constants.Role;
import com.oneone.event.EventMainTabSelection;
import com.oneone.framework.ui.BaseMainFragment;
import com.oneone.framework.ui.navigation.BottomNavigation;
import com.oneone.framework.ui.navigation.TabItem;
import com.oneone.framework.ui.utils.SvgUtil;
import com.oneone.framework.ui.widget.ScrollableViewPager;
import com.oneone.modules.main.discover.FindFragment;
import com.oneone.modules.main.me.Mine4MatcherFragment;
import com.oneone.modules.main.me.Mine4SingleFragment;
import com.oneone.modules.main.message.MsgFragment;
import com.oneone.modules.main.singles.SinglesFragment;
import com.oneone.modules.main.timeline.TimeLineFragment;
import com.oneone.modules.user.HereUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class FragmentTabBuilder {
    private boolean isMatcher = false;
    private List<BaseMainFragment> mFragments = new ArrayList<>();
    private List<TabItem> mTabItems = new ArrayList<>();
    BottomNavigation mBottomNavigation;

    public FragmentTabBuilder initFragmentAndTab(Context context, ScrollableViewPager mViewPager,
                                                 BottomNavigation mBottomNavigation,
                                                 FragmentManager fragmentManager,
                                                 ScrollableViewPager.OnPageChangeListener listener) {
        initFragments(context);

        this.mBottomNavigation = mBottomNavigation;
        AdapterTabFragment mAdapterTabFragment = new
                AdapterTabFragment(fragmentManager, mFragments);

        mBottomNavigation.bindViewPager(mViewPager, mFragments);
        mViewPager.setAdapter(mAdapterTabFragment);
        mViewPager.setScrollable(false);
        mBottomNavigation.setTabItems(mTabItems);
        mViewPager.addOnPageChangeListener(listener);
        setCurrentTab("");
        return this;
    }

    public void setCurrentTab(String tabCurrent) {
        int index = getTabIndexByTag(tabCurrent);
        mBottomNavigation.setSelectedItem(index);
    }

    public void handleFragmentOnNewIntent(String tabCurrent, Intent intent) {
        if (!TextUtils.isEmpty(tabCurrent)) {
            setCurrentTab(tabCurrent);
        }

        for (BaseMainFragment fragment : mFragments) {
            fragment.onNewIntent(intent);
        }
    }

    private void initFragments(Context context) {
        if (HereUser.getInstance() == null || HereUser.getInstance().getUserInfo() == null) {
            return;
        }
        isMatcher = HereUser.getInstance().getUserInfo().getRole() == Role.MATCHER;
        BaseMainFragment fragTimeLine = getTimeLineFrag(context);
        addFragmentAndTab(fragTimeLine);

        if (isMatcher) {
            BaseMainFragment fragSingles = getSinglesFrag(context);
            addFragmentAndTab(fragSingles);

            BaseMainFragment fragMine = getMe4MatcherFrag(context);
            addFragmentAndTab(fragMine);
        } else {
            BaseMainFragment fragFind = getFindFrag(context);
            addFragmentAndTab(fragFind);

            BaseMainFragment fragMsg = getMsgFrag(context);
            addFragmentAndTab(fragMsg);

            BaseMainFragment fragMine = getMe4SingleFrag(context);
            addFragmentAndTab(fragMine);
        }
    }


    public List<BaseMainFragment> getFragments() {
        return mFragments;
    }

    public List<TabItem> getTabItems() {
        return mTabItems;
    }

    public int getTabIndexByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return 1;
        }

        for (int i = 0; i < mTabItems.size(); i++) {
            TabItem item = mTabItems.get(i);
            if (item.getFagmentTag().equals(tag)) {
                return i;
            }
        }
        return 1;
    }

    @NonNull
    private BaseMainFragment getMsgFrag(Context context) {
        TabItem itemMsg = new TabItem(context, EventMainTabSelection.TAB_MESSAGE);
        itemMsg.setText(context.getString(R.string.str_tab_msg));
        itemMsg.setSelectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_msg_selected));
        itemMsg.setUnselectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_msg_normal));
        itemMsg.setUnselectedTabTextColor(getColor(context, R.color.color_black));
        itemMsg.setSelectedTabTextColor(getColor(context, R.color.color_black));
        BaseMainFragment msg = (MsgFragment)
                Fragment.instantiate(context, MsgFragment.class.getName());
        msg.setTabItem(itemMsg);

        return msg;
    }

    @NonNull
    private BaseMainFragment getFindFrag(Context context) {
        TabItem itemFind = new TabItem(context, EventMainTabSelection.TAB_EXPLORE);
        itemFind.setText(context.getString(R.string.str_tab_find));
        itemFind.setUnselectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_find_normal));
        itemFind.setSelectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_find_selected));
        itemFind.setUnselectedTabTextColor(getColor(context, R.color.color_black));
        itemFind.setSelectedTabTextColor(getColor(context, R.color.color_black));
        BaseMainFragment find = (FindFragment)
                Fragment.instantiate(context, FindFragment.class.getName());
        find.setTabItem(itemFind);
        return find;
    }

    @NonNull
    private BaseMainFragment getSinglesFrag(Context context) {
        TabItem itemSingles = new TabItem(context, EventMainTabSelection.TAB_SINGLES);
        itemSingles.setText(context.getString(R.string.str_tab_singles));
        itemSingles.setUnselectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_singles_normal));
        itemSingles.setSelectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_singles_selected));
        itemSingles.setUnselectedTabTextColor(getColor(context, R.color.color_black));
        itemSingles.setSelectedTabTextColor(getColor(context, R.color.color_black));
        BaseMainFragment singles = (SinglesFragment)
                Fragment.instantiate(context, SinglesFragment.class.getName());
        singles.setTabItem(itemSingles);
        return singles;
    }

    @NonNull
    private BaseMainFragment getTimeLineFrag(Context context) {
        TabItem itemTimeLine = new TabItem(context, EventMainTabSelection.TAB_TIMELINE);
        itemTimeLine.setText(context.getString(R.string.str_tab_timeline));
        itemTimeLine.setUnselectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_timeline_normal));
        itemTimeLine.setSelectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_timeline_selected));
        itemTimeLine.setUnselectedTabTextColor(getColor(context, R.color.color_black));
        itemTimeLine.setSelectedTabTextColor(getColor(context, R.color.color_black));

        BaseMainFragment timeLine = (TimeLineFragment)
                Fragment.instantiate(context, TimeLineFragment.class.getName());
        timeLine.setTabItem(itemTimeLine);
        return timeLine;
    }

    @NonNull
    private BaseMainFragment getMe4MatcherFrag(Context context) {
        TabItem itemMine = new TabItem(context, EventMainTabSelection.TAB_ME);
        itemMine.setText(context.getString(R.string.str_tab_mine));

        itemMine.setUnselectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_mine_normal));
        itemMine.setSelectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_mine_selected));
        itemMine.setUnselectedTabTextColor(getColor(context, R.color.color_black));
        itemMine.setSelectedTabTextColor(getColor(context, R.color.color_black));

        BaseMainFragment fragmentMine = (Mine4MatcherFragment)
                Fragment.instantiate(context, Mine4MatcherFragment.class.getName());
        fragmentMine.setTabItem(itemMine);
        return fragmentMine;
    }

    @NonNull
    private BaseMainFragment getMe4SingleFrag(Context context) {
        TabItem itemMine = new TabItem(context, EventMainTabSelection.TAB_ME);
        itemMine.setText(context.getString(R.string.str_tab_mine));

        itemMine.setUnselectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_mine_normal));
        itemMine.setSelectedTabIcon(SvgUtil.getDrawable(context, R.drawable.vector_drawable_mine_selected));
        itemMine.setUnselectedTabTextColor(getColor(context, R.color.color_black));
        itemMine.setSelectedTabTextColor(getColor(context, R.color.color_black));

        BaseMainFragment fragmentMine = (Mine4SingleFragment)
                Fragment.instantiate(context, Mine4SingleFragment.class.getName());

        fragmentMine.setTabItem(itemMine);
        return fragmentMine;
    }

    private void addFragmentAndTab(BaseMainFragment fragment) {
        mFragments.add(fragment);
        mTabItems.add(fragment.getTabItem());
    }

    private Drawable getDrawable(Context context, @DrawableRes int id) {
        return context.getResources().getDrawable(id);
    }

    private int getColor(Context context, @ColorRes int id) {
        return context.getResources().getColor(id);
    }
}
