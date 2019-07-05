package com.oneone.framework.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;


public abstract class AbstractPullListFragment<T, P extends IPresenter<V>, V extends IBaseView> extends BasePresenterFragment<P, V>
        implements IBaseView, OnRefreshLoadMoreListener {

    private BaseRecyclerViewAdapter<T> mAdapter;
    private int pageIdx = 0;
    private SimplePullRecyclerView<T> mRecyclerView;

    @Override
    public P onPresenterCreate() {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView= getDisplayView();
        mAdapter = adapterToDisplay();
        View mEmptyView = getEmptyView();
        if (mEmptyView != null && mEmptyView.getParent() == null) {
            mRecyclerView.setEmptyView(mEmptyView);
        }

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        mRecyclerView.setAdapter(mAdapter, layoutManager);
        mRecyclerView.setOnRefreshLoadMoreListener(this);
            RefreshFooter footer = getFooterView();
            if (footer != null) {
                mRecyclerView.setRefreshFooter(footer);
            }

            RefreshHeader header = getHeaderView();
            if (header != null) {
                mRecyclerView.setRefreshHeader(header);
            }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (loadRefreshOnStart()) {
            mRecyclerView.autoRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    public abstract BaseRecyclerViewAdapter<T> adapterToDisplay();

    @NonNull
    public abstract SimplePullRecyclerView<T> getDisplayView();

    protected RefreshFooter getFooterView() {
        return null;
    }

    protected RefreshHeader getHeaderView() {
        return null;
    }

    public View getEmptyView() {
        return null;
    }

    public boolean loadRefreshOnStart() {
        return false;
    }

    public void loadMore(int pageIndex) {

    }

    protected void setRefreshEnable(boolean enable) {
        mRecyclerView.setRefreshEnable(enable);
    }

    protected void setLoadMoreEnable(boolean enable) {
        mRecyclerView.setLoadMoreEnable(enable);
    }

    public void loadRefresh() {
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    /**
     * After the load is called,
     * the data is returned to call this function to display the data
     *
     * @param data loaded data
     */
    protected void onLoadCompleted(List<T> data) {
        mRecyclerView.onRefreshCompleted(data);
    }

    protected void onLoadMore(List<T> data) {
        mRecyclerView.onLoadMoreCompleted(data);
    }

    protected void onLoadFailed(String info) {
        mRecyclerView.onLoadFailed(info);
    }

    protected BaseRecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    public boolean shouldRefreshingHeaderOnStart() {
        return false;
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
        pageIdx++;
        loadMore(pageIdx);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        pageIdx = 0;
        loadRefresh();
    }

}
