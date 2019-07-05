package com.oneone.framework.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * The {@link BaseViewHolder} is used by view adapter
 *
 * @param <T>
 * @author qingfei.chen
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    @SuppressWarnings("unchecked")
    public static <T extends BaseViewHolder<?>> T as(View v) {
        return (T) v.getTag();
    }

    private final View view;

    private T mData;

    protected ItemClickListener<T> mListener;

    /**
     * Create an instance with the specified view
     *
     * @param v The root view to hold
     */
    protected BaseViewHolder(View v) {
        super(v);
        this.view = v;
        v.setTag(this);
        ButterKnife.bind(this, v);
    }

    protected BaseViewHolder(View v, ItemClickListener<T> listener) {
        this(v);
        this.mListener = listener;
    }

    /**
     * Returns the context
     *
     * @return the context
     */
    public Context getContext() {
        return this.view.getContext();
    }

    /**
     * Bind the specified view
     *
     * @param t The object to bound
     */
    public void bind(T t, int position) {
        this.mData = t;
    }

    public T getData() {
        return mData;
    }

    /**
     * If you needed ,you can implement this method
     */
    public interface ItemClickListener<T> {
        void onItemClick(T t, int id, int position);
    }

    protected View getConvertView() {
        return view;
    }

}
