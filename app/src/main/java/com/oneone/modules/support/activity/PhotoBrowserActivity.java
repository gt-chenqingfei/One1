package com.oneone.modules.support.activity;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.oneone.framework.ui.imagepicker.preview.PhotoBrowserPagerActivity;

import java.util.ArrayList;

/**
 * @author qingfei.chen
 * @since 2018/7/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@Route(path = "/photo/browser")
public class PhotoBrowserActivity extends PhotoBrowserPagerActivity {
    public static void launch(Context mContext, String normalPath) {
        Intent intent = new Intent(mContext, PhotoBrowserPagerActivity.class);
        ArrayList<String> normalPaths = new ArrayList<>();
        normalPaths.add(normalPath);
        intent.putExtra(EXTRA_PATH, normalPaths);
        intent.putExtra(EXTRA_IS_SHOW_NUMBERS, false);
        mContext.startActivity(intent);
    }
}
