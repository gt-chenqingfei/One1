package com.oneone.framework.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.List;


/**
 * @author qingfei.chen
 * @since 2017-01-09 23:40:52
 */

public class SimplePullRecyclerView<T> extends FrameLayout {

    private SwipeMenuRecyclerView mRecyclerView;
    private FrameLayout mFlFooterContainer;
    private SmartRefreshLayout mSmartRefreshLayout;
    private List<T> mData;
    private BaseRecyclerViewAdapter<T> mSimpleAdapter;
    private FrameLayout mEmptyContainer;
    private boolean noMoreData = false;

    public void setData(List<T> data) {
        this.mData = data;
        this.mSimpleAdapter.notifyDataSetChanged();
    }

    public void setFooter(View footer) {
        this.mFlFooterContainer.removeAllViews();
        this.mFlFooterContainer.addView(footer);
    }

    public void setNoMoreData(boolean noData) {
        noMoreData = noData;
        if (noMoreData) {
            mFlFooterContainer.setVisibility(View.VISIBLE);
        } else {
            mFlFooterContainer.setVisibility(View.GONE);
        }
    }

    public SimplePullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.simple_pull_recycler_view, this);
        this.mRecyclerView = findViewById(R.id.simple_pull_rv_recycler_view);
        this.mFlFooterContainer = findViewById(R.id.simple_pull_fl_container);
        this.mSmartRefreshLayout = findViewById(R.id.simple_pull_refresh_layout);
        this.mEmptyContainer = findViewById(R.id.simple_pull_empty_view);

        initListener();
    }

    private void initListener() {
    }

    public SimplePullRecyclerView(Context context) {
        this(context, null);
    }

    public void setAdapter(BaseRecyclerViewAdapter adapter, RecyclerView.LayoutManager layoutManager) {
        this.mSimpleAdapter = adapter;
        mSimpleAdapter.setEmptyView(mEmptyContainer);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setAdapter(mSimpleAdapter);
        this.mRecyclerView.setNestedScrollingEnabled(false);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void setEmptyView(View v) {
        if (v != null) {
            mEmptyContainer.removeAllViews();
            mEmptyContainer.addView(v);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SmartRefreshLayout getSmartRefreshLayout() {
        return mSmartRefreshLayout;
    }

    public boolean isNoMoreData() {
        return noMoreData;
    }

    /**
     * After the load is called,
     * the data is returned to call this function to display the data
     *
     * @param data loaded data
     */
    public void onRefreshCompleted(List<T> data) {
        mSimpleAdapter.notifyDataChange(data);
        mSmartRefreshLayout.finishRefresh();
    }

    public void onLoadMoreCompleted(List<T> data) {
        mSimpleAdapter.notifyDataAdd(data);
        if (data == null || data.isEmpty()) {
            mSmartRefreshLayout.finishLoadMore(true);
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    public void onLoadFailed(String info) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    public void setRefreshEnable(boolean enable) {
        mSmartRefreshLayout.setEnableRefresh(enable);
    }

    public void setLoadMoreEnable(boolean enable) {
        mSmartRefreshLayout.setEnableLoadMore(enable);
    }

    public void autoRefresh(){
        mSmartRefreshLayout.autoRefresh();
    }

    public void setRefreshHeader(RefreshHeader header ){
        mSmartRefreshLayout.setRefreshHeader(header);
    }
    public void setRefreshFooter(RefreshFooter footer) {
        mSmartRefreshLayout.setRefreshFooter(footer);
    }

    public void setOnRefreshLoadMoreListener(OnRefreshLoadMoreListener listener){
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(listener);
    }

    public RefreshHeader getRefreshHeader() {
        return mSmartRefreshLayout.getRefreshHeader();
    }

}
