package com.oneone;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.oneone.framework.ui.AbstractPullListActivity;
import com.oneone.framework.ui.BaseRecyclerViewAdapter;
import com.oneone.framework.ui.ibase.IBaseView;
import com.oneone.framework.ui.ibase.IPresenter;
import com.oneone.widget.CustomGlobalHeader;
import com.oneone.framework.ui.widget.SimplePullRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;

public abstract class BasePullListActivity<T, P extends IPresenter<V>, V extends IBaseView> extends AbstractPullListActivity<T, P, V> {

    protected BasePullListActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    @Override
    protected RefreshHeader getHeaderView() {
        return new CustomGlobalHeader(mContext);
    }
}
