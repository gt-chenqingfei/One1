package com.oneone.framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.WindowManager;

import com.oneone.framework.ui.annotation.LayoutResource;
import com.oneone.framework.ui.utils.ScreenUtil;

import butterknife.ButterKnife;

/**
 * @author qingfei.chen
 * @since 2018/1/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Class<?> clazz = getClass();
        final LayoutResource layout = clazz.getAnnotation(LayoutResource.class);
        if (layout != null) {
            setContentView(layout.value());
        }
        ButterKnife.bind(this);
        setFullScreen();
    }

    protected boolean isFullScreen() {
        return false;
    }

    private void setFullScreen() {
        if (isFullScreen()) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = ScreenUtil.getDisplayWidth();
            lp.height = ScreenUtil.getDisplayHeight();
            getWindow().setAttributes(lp);
        }
    }
}
