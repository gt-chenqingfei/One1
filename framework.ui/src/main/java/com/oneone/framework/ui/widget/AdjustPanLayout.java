package com.oneone.framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.oneone.framework.ui.R;


/**
 * @author qingfei.chen
 * @since 2018/1/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public class AdjustPanLayout extends FrameLayout {
    private int scrollLayoutId;

    public AdjustPanLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AdjustPanLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AdjustPanLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                           @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AdjustPanLayout);
        scrollLayoutId = typedArray.getResourceId(R.styleable.AdjustPanLayout_scrollLayoutId, -1);

        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View scrollLayout = this.findViewById(scrollLayoutId);
        if (scrollLayout != null) {
            addScrollLayoutListener(this, scrollLayout);
        }
    }

    public void addScrollLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int screenHeight = main.getRootView().getHeight();
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;

                Rect rectScroll = new Rect();
                scroll.getGlobalVisibleRect(rectScroll);
                int scrollVisibleHeight = main.getRootView().getHeight() - rectScroll.bottom;
                if (scrollVisibleHeight <= mainInvisibleHeight) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, scrollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }
}
