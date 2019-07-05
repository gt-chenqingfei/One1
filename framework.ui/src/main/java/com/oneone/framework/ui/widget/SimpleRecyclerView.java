package com.oneone.framework.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.R;

import java.util.List;


/**
 * @author qingfei.chen
 * @since 2017-01-09 23:40:52
 */

public class SimpleRecyclerView<T> extends FrameLayout {

    private RecyclerView mRecyclerView;
    private FrameLayout mFlContainer;
    private BaseRecyclerViewAdapter<T> mSimpleAdapter;

    public void setData(List<T> data) {
        mSimpleAdapter.notifyDataChange(data);
    }

    public void setHeader(View header) {
        this.mFlContainer.removeAllViews();
        this.mFlContainer.addView(header);
    }

    public SimpleRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.simple_recycler_view, this);
        this.mRecyclerView = findViewById(R.id.simple_rv_recycler_view);
        this.mFlContainer =  findViewById(R.id.simple_fl_container);

    }

    public SimpleRecyclerView(Context context) {
        this(context, null);
    }

    public void setAdapter(BaseRecyclerViewAdapter<T> adapter) {
        this.mSimpleAdapter = adapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.setAdapter(mSimpleAdapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
