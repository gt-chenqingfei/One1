package com.oneone.framework.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneone.framework.android.utils.Toasts;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;


public abstract class BasePresenterFragment<P extends IPresenter<V>, V extends IBaseView> extends BaseFragment
        implements IBaseView {

    protected P mPresenter;

    public abstract P onPresenterCreate();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = onPresenterCreate();
        if (mPresenter != null) {
            mPresenter.onAttachView((V) this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
    }

    @Override
    public void loading(String msg) {
        showLoading(msg);
    }

    @Override
    public void loadingDismiss() {
        hideLoading();
    }


    @Override
    public void showError(final String errorMsg) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasts.show(getActivityContext(), errorMsg);
            }
        });
    }

    @Override
    public FragmentActivity getActivityContext() {
        return getActivity();
    }
}
