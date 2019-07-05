package com.oneone.framework.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oneone.framework.ui.annotation.LayoutResource;

import butterknife.ButterKnife;

/**
 * @author qingfei.chen
 * @since 17/12/22.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public abstract class BaseView extends LinearLayout {

    protected Context mContext;

    abstract public void onViewCreated();

    private AttributeSet attrs;

    public BaseView(Context context) {
        super(context);
        mContext = context;
        createView(this, false);
    }

    public BaseView(Context context, boolean attachToRoot) {
        super(context);
        mContext = context;
        createView(this, attachToRoot);
    }

    public BaseView(Context context, ViewGroup vg, boolean attachToRoot) {
        super(context);
        mContext = context;
        createView(vg, attachToRoot);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        mContext = context;
        createView(this, true);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        mContext = context;
        createView(this, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.attrs = attrs;
        mContext = context;
        createView(this, true);
    }

    public void createView(ViewGroup vg, boolean attachToRoot) {
        final Class<?> clazz = getClass();
        final LayoutResource layout = clazz.getAnnotation(LayoutResource.class);
        if (layout != null) {
            ViewGroup contentView = (ViewGroup) LayoutInflater.from(mContext).inflate(layout.value(), vg, attachToRoot);
            ButterKnife.bind(this, contentView);

            if (!attachToRoot) {
                this.addView(contentView);
            }
        } else {
            ButterKnife.bind(this, this);
        }

        onViewCreated();
    }

    public AttributeSet getAttrs() {
        return attrs;
    }
}
