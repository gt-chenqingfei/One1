package com.oneone.framework.ui.ibase;


public interface IPresenter<V extends IBaseView> {

    /**
     * Presenter与View建立连接
     *
     * @param mvpView 与此Presenter相对应的View
     */
    void onAttachView(V mvpView);

    /**
     * Presenter与View连接断开
     */
    void onDetachView();

    void showError(String errorMessage);
}