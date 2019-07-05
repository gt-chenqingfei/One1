package com.oneone.framework.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.oneone.framework.android.utils.Toasts;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.widget.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2017/8/2.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public abstract class BaseListActivity<T, P extends IPresenter<V>, V extends IBaseView>
        extends AbstractBaseActivity<P, V> implements IBaseView {

    private BaseRecyclerViewAdapter<T> mAdapter;
    private List<AsyncTask> mTaskQueue = new ArrayList<>();

    @Override
    public P onCreatePresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleRecyclerView<T> view = getDisplayView();
        if (view.getParent() == null) {
            setContentView(view);
        }
        mAdapter = adapterToDisplay();
        view.setAdapter(mAdapter);
        if (autoLoad()) {
            load();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mTaskQueue.isEmpty()) {
            for (AsyncTask task : mTaskQueue) {
                if (task != null && !task.isCancelled()) {
                    task.cancel(true);
                }
            }
        }
    }

    protected boolean autoLoad() {
        return true;
    }

    protected void load() {

        AsyncTask task = new AsyncTask<String, Void, List<T>>() {
            @Override
            protected List<T> doInBackground(String... params) {
                return doLoad();
            }

            @Override
            protected void onPostExecute(List<T> data) {
                onLoadCompleted(data);
            }
        }.execute();

        mTaskQueue.add(task);
    }

    @NonNull
    public abstract BaseRecyclerViewAdapter<T> adapterToDisplay();

    @NonNull
    public abstract SimpleRecyclerView<T> getDisplayView();

    /**
     * This function is called after the onCreate
     * and is used to load the display data
     * This method is run  background
     */
    public List<T> doLoad() {
        return null;
    }



    /**
     * After the load is called,
     * the data is returned to call this function to display the data
     *
     * @param data loaded data
     */
    protected void onLoadCompleted(List<T> data) {
        mAdapter.notifyDataChange(data);
    }

    protected BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void loading(String msg) {

    }

    @Override
    public void loadingDismiss() {

    }

    @Override
    public void showError(String errorMsg) {
        Toasts.show(this, errorMsg);
    }

    @Override
    public FragmentActivity getActivityContext() {
        return this;
    }
}
