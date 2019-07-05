package com.oneone.modules.profile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.oneone.modules.profile.fragment.AbstractProfileFragment;

import java.util.List;


public class TabLayoutPagerAdapter extends FragmentPagerAdapter {

    List<AbstractProfileFragment> mFragments;
    String[] mTitles;

    public TabLayoutPagerAdapter(FragmentManager fm, List<AbstractProfileFragment> fragments, String[] titles) {
        this(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    public TabLayoutPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
