package com.oneone.modules.profile.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.oneone.R;
import com.oneone.framework.ui.BaseFragment;
import com.oneone.framework.ui.BasePresenterFragment;
import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.widget.MeasureViewPager;
import com.oneone.modules.profile.presenter.ProfilePresenter;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;

/**
 * @author qingfei.chen
 * @since 2018/5/9.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@SuppressLint("ValidFragment")
public abstract class AbstractProfileFragment<P extends IPresenter<V>, V extends IBaseView> extends BasePresenterFragment<P, V> {

    private MeasureViewPager pager;
    private int position;

    AbstractProfileFragment(MeasureViewPager pager, int position) {
        this.pager = pager;
        this.position = position;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pager != null)
            pager.setObjectForPosition(view, position);
    }

    public abstract void bindUserInfo(UserInfo userInfo);

    public abstract void bindProfilePresenter(ProfilePresenter presenter);

    public abstract void onPullRefresh();

    public void onLoadMore() {
    }

    public int getPosition() {
        return position;
    }
}
