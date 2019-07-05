package com.oneone.framework.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.widget.Toast;

import com.oneone.framework.android.utils.Toasts;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;

/**
 * @author qingfei.chen
 * @since 2018/7/16.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public abstract class BasePresenterView<P extends IPresenter<V>, V extends IBaseView>  extends BaseView
implements IBaseView{
    protected P mPresenter;

    public BasePresenterView(Context context) {
        super(context);
    }

    public BasePresenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {
        mPresenter = onCreatePresenter();
        mPresenter.onAttachView((V)this);
    }

    public abstract P onCreatePresenter();

    @Override
    public void loading(String msg) {

    }

    @Override
    public void loadingDismiss() {

    }

    @Override
    public void showError(String errorMsg) {
        Toasts.show(getContext(),errorMsg, Toast.LENGTH_LONG);
    }

    @Override
    public FragmentActivity getActivityContext() {
        return (FragmentActivity)getContext();
    }
}
