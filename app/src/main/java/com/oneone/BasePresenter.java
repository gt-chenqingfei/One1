package com.oneone;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.oneone.framework.android.schedule.AsyncTaskQueue;
import com.oneone.framework.android.schedule.AsyncTaskQueueFactory;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;

public abstract class BasePresenter<V extends IBaseView> implements IPresenter<V> {

    private V mView;
    private AsyncTaskQueue asyncTaskQueue;

    @Override
    public void onAttachView(V view) {
        this.mView = view;
        this.asyncTaskQueue = AsyncTaskQueueFactory.newTaskQueue(view.getActivityContext());
    }

    @Override
    public void onDetachView() {
        this.mView = null;
        this.asyncTaskQueue.stop();
//        this.asyncTaskQueue.cancelAll(this);
    }

    @Override
    public void showError(String errorMessage) {
        if (mView != null) {
            if (!TextUtils.isEmpty(errorMessage)) {
                mView.showError(errorMessage);
            }
            mView.loadingDismiss();
        }
    }

    public boolean isViewAttached() {
        return mView != null;
    }

    public V getView() {
        return mView;
    }

    public <Params, Progress, Result> void enqueue(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        asyncTaskQueue.add(task, params);
    }

    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call onAttachView(MvpView) before request data !");
        }
    }
}
