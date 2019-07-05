package com.oneone.modules.qa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oneone.modules.qa.fragment.AlreadyAnswerQuestionListFragment;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public class MyQaClassifyPagerFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private List<AlreadyAnswerQuestionListFragment> mlist;

    public MyQaClassifyPagerFragmentAdapter(FragmentManager fm, List<AlreadyAnswerQuestionListFragment> list) {
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
