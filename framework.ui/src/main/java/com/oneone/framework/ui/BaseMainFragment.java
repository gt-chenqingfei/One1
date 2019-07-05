package com.oneone.framework.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.immersionbar.ImmersionBar;
import com.oneone.framework.ui.navigation.TabItem;

import org.slf4j.LoggerFactory;

/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public abstract class BaseMainFragment<P extends IPresenter<V>, V extends IBaseView> extends
        BasePresenterFragment<P, V> {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private TabItem mTabItem;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), getTitleView());
    }

    @Override
    public P onPresenterCreate() {
        return null;
    }

    public void setTabItem(TabItem tabItem) {
        this.mTabItem = tabItem;
    }

    public TabItem getTabItem() {
        return mTabItem;
    }

    public void setBubble(int bubble) {
        String bubbleStr = getBubbleStr(bubble);
        if (mTabItem != null) {
            mTabItem.setBubble(bubbleStr);
        }
    }

    public String getBubbleStr(int bubble) {
        String bubbleStr = "";
        if (bubble >= 100) {
            bubbleStr = "99+";
        } else if (bubble > 0) {
            bubbleStr = bubble + "";
        }
        return bubbleStr;
    }

    public void showDot(boolean isShow) {
        if (mTabItem != null) {
            mTabItem.showDot(isShow);
        }
    }

    public void onAppear() {
        // fragment appear
        logger.info("onAppear");
    }

    public void onDisAppear() {
        // fragment disappear
        logger.info("onDisAppear");
    }

    public void onTabDoubleTap() {
        logger.info("onTabDoubleTap");
    }

    public abstract View getTitleView();

    public void onNewIntent(Intent intent) {

    }
}
