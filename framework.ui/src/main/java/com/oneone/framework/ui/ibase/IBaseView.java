package com.oneone.framework.ui.ibase;

import android.support.v4.app.FragmentActivity;


public interface IBaseView {

    /**
     * 显示loading对话框
     *
     * @param msg
     */
    void loading(String msg);

    /**
     * 隐藏loading对话框
     */
    void loadingDismiss();

    /**
     * 显示错误信息
     *
     * @param errorMsg
     */
    void showError(String errorMsg);

    /**
     * 获取当前的Activity
     *
     * @return
     */
    FragmentActivity getActivityContext();

}
