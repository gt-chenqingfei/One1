package com.oneone;

import android.content.Context;
import android.os.Handler;

import com.oneone.event.EventFinishActivity;
import com.oneone.framework.android.ApplicationContext;
import com.oneone.modules.main.MainActivity;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.dto.GiftProdDto;
import com.oneone.modules.support.model.SupportModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author qingfei.chen
 * @version v1.0.0
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 * @since 2018/6/7.
 */
public class AppInitializer {
    private static Logger logger = LoggerFactory.getLogger("AppInitializer");

    public interface OnLoadPreDataListener {
        void onLoadPreData(boolean isAppLaunched);
    }

    private ApplicationContext context;
    private static AppInitializer initializer = null;
    private volatile boolean isAppLaunched = false;
    private Handler mainHandler = new Handler();

    private AppInitializer(ApplicationContext context) {
        this.context = context;
    }

    public synchronized static void init(ApplicationContext context) {
        if (initializer != null) {
            logger.warn("AppInitializer has init!!!");
            return;
        }
        initializer = new AppInitializer(context);
    }

    public void unInit() {
        isAppLaunched = false;
        IMManager.getInstance().logout();
        HereSingletonFactory.getInstance().destory();
    }

    public static AppInitializer getInstance() {
        return initializer;
    }

    public void startMainAndLoadPreData(final Context context) {
        startMainAndLoadPreData(context, null);
    }

    public void startMainAndResetPreData(final Context context, OnLoadPreDataListener onLoadPreDataListener) {
        isAppLaunched = false;
        startMainAndLoadPreData(context, onLoadPreDataListener);
    }

    public synchronized void startMainAndLoadPreData(final Context context,
                                                     final OnLoadPreDataListener onLoadPreDataListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initAppPreDataBackground();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.startActivity(context);
                        EventBus.getDefault().post(new EventFinishActivity());
                        if (onLoadPreDataListener != null) {
                            onLoadPreDataListener.onLoadPreData(isAppLaunched);
                        }
                        isAppLaunched = true;
                    }
                });
            }
        }, "Initializing app").start();
    }

    private void initAppPreDataBackground() {
        if (isAppLaunched) {
            return;
        }

        try {
            UserManager manager = HereSingletonFactory.getInstance().getUserManager();
            manager.fetchUserInfoSync();
            ApiResult<List<IMEmoji>> result3 = IMManager.getInstance().initEmoji(context);
            if (result3 != null && result3.getData() != null && result3.getData().size() > 0) {
                IMManager.IM_EMOJI_LIST.clear();
                IMManager.IM_EMOJI_LIST.addAll(result3.getData());
            }
            ApiResult<GiftProdDto> result4 = IMManager.getInstance().initGift(context);
            if (result4 != null && result4.getData() != null) {
                if (result4.getData().getCount() > 0 && result4.getData().getList() != null) {
                    IMManager.GIFT_PROD_LIST.clear();
                    IMManager.GIFT_PROD_LIST.addAll(result4.getData().getList());
                }
            }

            IMManager.modifyUserInfo(HereUser.getInstance().getUserInfo());
            IMManager.getInstance().reset();
            IMManager.getInstance().login(null, context);

            new SupportModel(context).getShareInfo();// 初始化分享数据

        } finally {
            isAppLaunched = false;
        }
    }
}
