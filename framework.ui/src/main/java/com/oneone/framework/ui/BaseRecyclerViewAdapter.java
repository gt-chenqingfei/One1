package com.oneone.framework.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018-3/31.
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private View mEmptyView;
    private boolean showNoMoreView;

    public String noMoreText;
    public static final int VIEW_TYPE_NO_MORE_ITEM = 999;

    public BaseRecyclerViewAdapter(BaseViewHolder.ItemClickListener<T> listener) {
        this.mListener = listener;
    }

    private List<T> mList = new ArrayList<>();

    protected BaseViewHolder.ItemClickListener<T> mListener;

    public void setEmptyView(View view) {
        this.mEmptyView = view;
    }

    public boolean isShowNoMoreView() {
        return showNoMoreView;
    }

    public void setShowNoMoreView(boolean showNoMoreView) {
        this.showNoMoreView = showNoMoreView;

        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        if (list != null && !list.isEmpty()) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
        showEmptyViewIfNeeded();
    }

    public void addData(List<T> list, int position) {
        if (list != null && !list.isEmpty()) {
            this.mList.addAll(position, list);
            notifyDataSetChanged();
        }
        showEmptyViewIfNeeded();
    }

    public void addItem(T data, int position) {
        if (data != null) {
            this.mList.add(position, data);
            notifyDataSetChanged();
        }
        showEmptyViewIfNeeded();
    }

    public List<T> removeItem(T item, int position) {
        if (item != null && mList.contains(item)) {
            mList.remove(item);
        } else if (position < mList.size()) {
            mList.remove(position);
        }
        notifyDataSetChanged();
        showEmptyViewIfNeeded();
        return mList;
    }

    public List<T> removeItem(int position) {
        if (position < mList.size()) {
            mList.remove(position);
        }
        notifyItemRemoved(position);
        showEmptyViewIfNeeded();
        return mList;
    }

    public void notifyDataChange(List<T> list) {
        if (list != null && !list.isEmpty()) {
            this.mList = list;
            notifyDataSetChanged();
        } else {
            clearList();
        }
        showEmptyViewIfNeeded();
    }

    public void notifyDataAdd(List<T> list) {
        if (list != null && !list.isEmpty()) {
            this.mList.addAll(list);
            notifyDataSetChanged();
            showEmptyViewIfNeeded();
        }
    }

    public void clearList() {
        mList.clear();
        notifyDataSetChanged();
        showEmptyViewIfNeeded();
    }

    public List<T> getList() {
        return mList;
    }

    @Override
    public int getItemCount() {
        if (showNoMoreView && mList != null && mList.size() > 0) {
            return mList == null ? 1 : mList.size() + 1;
        } else {
            return mList == null ? 0 : mList.size();
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        T curItem = null;
        if (position < mList.size()) {
            curItem = mList.get(position);
        }
        holder.bind(curItem, position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    /**
     * @param item element to search for
     * @return the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     */
    public int index(T item) {
        if (mList == null || mList.isEmpty()) {
            return -1;
        }
        return mList.indexOf(item);
    }

    public void showEmptyViewIfNeeded() {
        if (mEmptyView == null) {
            return;
        }
        int itemCount = getItemCount();
        if (isContainsHeader()) {
            itemCount--;
        }
        mEmptyView.setVisibility(itemCount <= 0 ? View.VISIBLE : View.GONE);
    }

    public boolean isContainsHeader() {
        return false;
    }
}
