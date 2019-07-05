package com.oneone;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.oneone.framework.ui.AbstractBaseActivity;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.utils.SystemBarTintManager;
import com.oneone.utils.ToastUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseActivity<P extends IPresenter<V>, V extends IBaseView> extends
        AbstractBaseActivity<P, V> implements Observer, IBaseView {
    private boolean destroyed = false;
    final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    protected BaseActivity mActivity;

    @Override
    public P onCreatePresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        logger.info(" onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger.info(" onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logger.info(" onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logger.info(" onRestoreInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger.info(" onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logger.info(" onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logger.info(" onDestroy");
        destroyed = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        recreate();
    }

    /**
     * 判断页面是否已经被销毁（异步回调时使用）
     */
    protected boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    @Override
    public void loading(String msg) {
        showLoading(msg);
    }

    @Override
    public void loadingDismiss() {
        dismissLoading();
    }

    @Override
    public void showError(String errorMsg) {
        ToastUtil.show(this,errorMsg);
    }

    @Override
    public FragmentActivity getActivityContext() {
        return this;
    }
}
