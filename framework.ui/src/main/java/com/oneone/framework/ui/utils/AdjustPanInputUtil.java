package com.oneone.framework.ui.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @author qingfei.chen
 * @since 2018/1/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public class AdjustPanInputUtil {

    /**
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public static void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int screenHeight = main.getRootView().getHeight();
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > screenHeight / 4) {
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
