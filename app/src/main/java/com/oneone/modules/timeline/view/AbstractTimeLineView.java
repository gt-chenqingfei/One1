package com.oneone.modules.timeline.view;

import android.content.Context;
import android.util.AttributeSet;

import com.oneone.framework.ui.BaseView;
import com.oneone.modules.timeline.contract.TimeLineContract;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public abstract class AbstractTimeLineView<T> extends BaseView {
    public AbstractTimeLineView(Context context) {
        super(context);
    }

    public AbstractTimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onViewCreated() {

    }

    public abstract void bind(T t, TimeLineContract.Presenter presenter, int position);

}
