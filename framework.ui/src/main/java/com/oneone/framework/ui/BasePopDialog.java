package com.oneone.framework.ui;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author qingfei.chen
 * @since 2017/9/7.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */

public class BasePopDialog extends BaseDialog {

    protected WindowManager.LayoutParams attr;

    public BasePopDialog(@NonNull Context context) {
        this(context, R.style.base_dialog);
    }

    public BasePopDialog(@NonNull Context context, @StyleRes int style) {
        super(context, style);
        init();
    }

    private void init() {
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.base_dialog_anim);
            window.getDecorView().setPadding(0, 0, 0, 0);
            attr = window.getAttributes();
            if (attr != null) {
                attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
                attr.gravity = Gravity.BOTTOM;
                attr.width = WindowManager.LayoutParams.MATCH_PARENT;
                attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(attr);
            }
        }
    }

}
