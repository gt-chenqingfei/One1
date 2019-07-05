package com.oneone.framework.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.widget.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseListFragment<D> extends BasePresenterFragment
        implements IBaseView {

    private BaseRecyclerViewAdapter<D> mAdapter;
    private List<AsyncTask> mTaskQueue = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SimpleRecyclerView<D> view = getDisplayView();
        mAdapter = adapterToDisplay();
        view.setAdapter(mAdapter);
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mTaskQueue.isEmpty()) {
            for (AsyncTask task : mTaskQueue) {
                if (task != null && !task.isCancelled()) {
                    task.cancel(true);
                }
            }
        }
    }

    protected void load() {

        AsyncTask task = new AsyncTask<String, Void, List<D>>() {
            @Override
            protected List<D> doInBackground(String... params) {
                return doLoad();
            }

            @Override
            protected void onPostExecute(List<D> data) {
                onLoadCompleted(data);
            }
        }.execute();

        mTaskQueue.add(task);
    }

    @NonNull
    public abstract BaseRecyclerViewAdapter<D> adapterToDisplay();

    @NonNull
    public abstract SimpleRecyclerView<D> getDisplayView();

    /**
     * This function is called after the onActivityCreated
     * and is used to load the display data
     * This method is run  background
     */
    public abstract List<D> doLoad();

    /**
     * After the load is called,
     * the data is returned to call this function to display the data
     *
     * @param data loaded data
     */
    protected void onLoadCompleted(List<D> data) {
        mAdapter.notifyDataChange(data);
    }

    protected BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }


    @Override
    public IPresenter onPresenterCreate() {
        return null;
    }
}
