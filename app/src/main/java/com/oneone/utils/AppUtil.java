package com.oneone.utils;

import android.content.Context;
import android.content.Intent;

import com.oneone.AppInitializer;
import com.oneone.framework.android.utils.ActivityUtils;

/**
 * @author qingfei.chen
 * @since 2018/7/13.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class AppUtil {
    public static void quit(Context context) {
        quit(context, false);
    }

    public static void quit(Context context, boolean kickout) {
        AppInitializer.getInstance().unInit();
        // 结束应用
        ActivityUtils.finishAllActivities();

        Intent intent = new Intent();
        intent.putExtra("EXTRA_KICK_OUT", kickout);
        intent.setClassName(context.getApplicationContext(),
                ActivityUtils.getLauncherActivity(context.getPackageName()));
        context.startActivity(intent);
    }
}
