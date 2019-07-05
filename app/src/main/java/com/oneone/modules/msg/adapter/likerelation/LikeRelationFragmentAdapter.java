package com.oneone.modules.msg.adapter.likerelation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oneone.modules.msg.fragment.likerelation.LikeRelationFragment;

import java.util.List;

/**
 * Created by here on 18/5/25.
 */

public class LikeRelationFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private List<LikeRelationFragment> mlist;

    public LikeRelationFragmentAdapter(FragmentManager fm, List<LikeRelationFragment> list) {
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
