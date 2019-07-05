package com.oneone.modules.following.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oneone.modules.following.fragment.MyFollowingFragment;

import java.util.List;

/**
 * Created by here on 18/4/26.
 */

public class MyFollowingFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private List<MyFollowingFragment> mlist;

    public MyFollowingFragmentAdapter(FragmentManager fm, List<MyFollowingFragment> list) {
        super(fm);
        this.mlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }
}
