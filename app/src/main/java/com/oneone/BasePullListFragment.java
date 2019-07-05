package com.oneone;

import android.support.annotation.NonNull;

import com.oneone.framework.ui.AbstractPullListFragment;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

public class BasePullListFragment<T, P extends IPresenter<V>, V extends IBaseView> extends AbstractPullListFragment<T, P, V> {
    @NonNull
    @Override
    public BaseRecyclerViewAdapter adapterToDisplay() {
        return null;
    }

    @NonNull
    @Override
    public SimplePullRecyclerView getDisplayView() {
        return null;
    }

    @Override
    protected RefreshHeader getHeaderView() {
        return new CustomGlobalHeader(getActivityContext());
    }
}
