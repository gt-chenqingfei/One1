package com.oneone.widget;

import android.content.Context;
import android.widget.TextView;

import com.oneone.R;
import com.oneone.framework.ui.BaseView;
import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.BindView;

/**
 * @author qingfei.chen
 * @since 2018/6/14.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@LayoutResource(R.layout.layout_nomore_data)
public class NoMorelData4Common extends BaseView {
    @BindView(R.id.layout_nomore_data_tv)
    TextView mTvNoMoreData;
    String text = "";

    public NoMorelData4Common(Context context, int textRes) {
        super(context);
        this.text = context.getString(textRes);
        setText(text);
    }

    @Override
    public void onViewCreated() {

    }

    public void setText(String text) {
        mTvNoMoreData.setText(text);
    }

    public void setText(int text) {
        mTvNoMoreData.setText(text);
    }
}
