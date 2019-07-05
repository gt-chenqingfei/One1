package com.oneone.modules.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.oneone.framework.ui.BaseMainFragment;

import java.util.List;

public class AdapterTabFragment extends FragmentPagerAdapter {
    private List<BaseMainFragment> mFragments;
    private FragmentManager fragmentManager;

    public AdapterTabFragment(FragmentManager fm, List<BaseMainFragment> fragments) {
        super(fm);
        this.fragmentManager = fm;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = getItem(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
