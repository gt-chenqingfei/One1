package com.oneone.framework.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oneone.framework.android.utils.Toasts;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author qingfei.chen
 * @since 2017/8/2.
 * Copyright © 2017  Technology Co.,Ltd. All rights reserved.
 */

public abstract class AbstractPullListActivity<T, P extends IPresenter<V>, V extends IBaseView>
        extends AbstractBaseActivity<P, V> implements OnRefreshLoadMoreListener,
        IBaseView {

    private BaseRecyclerViewAdapter<T> mAdapter;
    private List<AsyncTask> mTaskQueue = new ArrayList<>();
    private int pageIdx = 0;
    private SimplePullRecyclerView<T> recyclerView;


    @Override
    public P onCreatePresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = getDisplayView();
        if (recyclerView.getParent() == null) {
            setContentView(recyclerView);
        }
        mAdapter = adapterToDisplay();
        View mEmptyView = getEmptyView();
        if (mEmptyView != null && mEmptyView.getParent() == null) {
            recyclerView.setEmptyView(mEmptyView);
        }
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        recyclerView.setAdapter(mAdapter, layoutManager);

        recyclerView.setOnRefreshLoadMoreListener(this);

            RefreshFooter footer = getFooterView();
            if (footer != null) {
                recyclerView.setRefreshFooter(footer);
            }

            RefreshHeader header = getHeaderView();
            if (header != null) {
                recyclerView.setRefreshHeader(header);
            }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (loadRefreshOnStart()) {
            recyclerView.autoRefresh();
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

    @NonNull
    public abstract BaseRecyclerViewAdapter<T> adapterToDisplay();

    @NonNull
    public abstract SimplePullRecyclerView<T> getDisplayView();

    protected RefreshHeader getHeaderView() {
        recyclerView.getSmartRefreshLayout().setEnableHeaderTranslationContent(false);
        recyclerView.getSmartRefreshLayout().setPrimaryColorsId(R.color.color_white, android.R.color.white);//全局设置主题颜色
        return new MaterialHeader(mContext).setShowBezierWave(false);
    }

    protected RefreshFooter getFooterView() {
        return null;
    }

    protected View getEmptyView() {
        return null;
    }

    protected boolean loadRefreshOnStart() {
        return false;
    }

    protected void loadMore(int pageIndex) {
    }

    protected void loadRefresh() {
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    /**
     * After the load is called,
     * the data is returned to call this function to display the data
     *
     * @param data loaded data
     */
    protected void onRefreshCompleted(List<T> data) {
        if(recyclerView == null){
            return;
        }
        recyclerView.onRefreshCompleted(data);
    }

    protected void notifyDataSetChange(List<T> data) {
        mAdapter.notifyDataChange(data);
    }

    protected void onLoadMoreCompleted(List<T> data) {
        if (recyclerView == null) {
            return;
        }
       recyclerView.onLoadMoreCompleted(data);
    }

    protected void onLoadFailed(String info) {
        if(recyclerView == null){
            return;
        }
        recyclerView.onLoadFailed(info);
    }

    protected void setRefreshEnable(boolean enable) {
        recyclerView.setRefreshEnable(enable);
    }

    protected void setLoadMoreEnable(boolean enable) {
        recyclerView.setLoadMoreEnable(enable);
    }

    protected BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageIdx++;
        loadMore(pageIdx);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        pageIdx = 0;
        loadRefresh();
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
    public void showError(final String errorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDismiss();
                Toasts.show(getActivityContext(), errorMsg);
            }
        });
    }

    @Override
    public FragmentActivity getActivityContext() {
        return this;
    }
}
