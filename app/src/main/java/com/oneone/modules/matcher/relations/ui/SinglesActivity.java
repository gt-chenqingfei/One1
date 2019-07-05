package com.oneone.modules.matcher.relations.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.modules.main.singles.SinglesFragment;


/**
 * @author qingfei.chen
 * @since 2018/4/20.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

@Route(path = "/single_group/list")
@LayoutResource(R.layout.activity_singles)
public class SinglesActivity extends BaseActivity {
    private static final String EXTRA_COUNT = "extra_count";

    public static void startActivity(Context context, int count) {
        Intent intent = new Intent(context, SinglesActivity.class);
        intent.putExtra(EXTRA_COUNT, count);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int count = getIntent().getIntExtra(EXTRA_COUNT, 0);
        if (count <= 0) {
            this.finish();
            SinglesInviteActivity.startActivity(this);
            return;
        }
        try {
            SinglesFragment fragment = new SinglesFragment();
            fragment.enableBackBtn(true);
            fragment.enableRightMenu(false);
            fragment.setSingleCount(count);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.activity_singles_frag_container, fragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
