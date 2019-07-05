package com.oneone.framework.ui.utils;

import android.support.annotation.StringRes;

import com.oneone.framework.android.ApplicationContext;

/**
 * @author qingfei.chen
 * @since 2018/2/1.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class Res {
    public static String getString(@StringRes int res) {
        return ApplicationContext.getInstance().getString(res);
    }
}
