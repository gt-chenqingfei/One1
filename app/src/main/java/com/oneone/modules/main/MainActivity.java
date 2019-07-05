package com.oneone.modules.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.oneone.BaseActivity;
import com.oneone.R;
import com.oneone.event.EventMainTabSelection;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.navigation.BottomNavigation;
import com.oneone.framework.ui.widget.ScrollableViewPager;
import com.oneone.modules.dogfood.dialog.DailyReceiveDogFoodDialog;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.reddot.RedDotManager;
import com.oneone.modules.task.contract.TaskContract;
import com.oneone.modules.task.presenter.TaskPresenter;
import com.oneone.modules.upgrate.CheckVersionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/3/31.
 */
@Route(path = "/home/tab")
@LayoutResource(R.layout.activity_main)
public class MainActivity extends BaseActivity<TaskPresenter, TaskContract.View>
        implements TaskContract.View, ScrollableViewPager.OnPageChangeListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @BindView(R.id.activity_main_bottom_navigation)
    BottomNavigation mBottomNavigation;

    @BindView(R.id.activity_main_vp)
    ScrollableViewPager mViewPager;
    FragmentTabBuilder tabBuilder;
    @Autowired
    String tabCurrent;

    @Autowired
    String childTabCurrent;

    @Override
    public TaskPresenter onCreatePresenter() {
        return new TaskPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

        tabBuilder = new FragmentTabBuilder().initFragmentAndTab(this, mViewPager,
                mBottomNavigation, getSupportFragmentManager(), this);

        CheckVersionManager.getInstance().checkServerVersion(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RedDotManager.getInstance().startLoopFetchDot();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMManager.getInstance().reset();
        RedDotManager.getInstance().stopLoopFetchDot();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        tabCurrent = getIntent().getStringExtra("tabCurrent");
        tabBuilder.handleFragmentOnNewIntent(tabCurrent, getIntent());
    }


    @Override
    public void onTaskLoginReceiveAward(int taskAward, int received) {
        if (received == 0)
            new DailyReceiveDogFoodDialog(MainActivity.this, taskAward).show();
    }

    @Override
    public void onPageChange(int position) {
        RedDotManager.getInstance().fetchAllDot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventTabSelection(EventMainTabSelection event) {
        tabCurrent = event.getTabSelection();
        tabBuilder.setCurrentTab(tabCurrent);
    }
}
