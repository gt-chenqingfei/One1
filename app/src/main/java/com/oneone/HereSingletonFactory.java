package com.oneone;

import android.content.Context;

import com.mob.tools.utils.ReflectHelper;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.framework.android.utils.ReflectUtils;
import com.oneone.modules.support.qiniu.PhotoUploadManager;
import com.oneone.modules.timeline.NewTimeLineManager;
import com.oneone.modules.timeline.bean.TimeLine;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.UserSP;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qingfei.chen
 * @since 2018/6/7.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class HereSingletonFactory {
    private static class SingletonHolder {
        private static HereSingletonFactory instance = new HereSingletonFactory();
    }

    private Map<Class<?>, Object> instancePool;

    private HereSingletonFactory() {
        this.instancePool = new HashMap<>();
    }

    public static HereSingletonFactory getInstance() {
        return SingletonHolder.instance;
    }

    public void destory() {
        if (instancePool == null) {
            return;
        }
        for (Class<?> key : instancePool.keySet()) {
            Object object = instancePool.get(key);
            if (object != null) {
                object = null;
            }
        }

        instancePool.clear();
        HereUser.removeHereUser();
        UserSP.destory();
        DefaultSP.getInstance().clear(OneOne.getInstance()).commit();
    }

    public UserManager getUserManager() {
        UserManager userManager = null;
        if (instancePool.get(UserManager.class) == null) {
            try {
                userManager = ReflectUtils.reflect(UserManager.class).create(OneOne.mContext).get();
                instancePool.put(UserManager.class, userManager);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            userManager = (UserManager) instancePool.get(UserManager.class);
        }
        return userManager;
    }

    public PhotoUploadManager getPhotoUploadManager() {
        PhotoUploadManager photoUploadManager = null;
        if (instancePool.get(PhotoUploadManager.class) == null) {
            try {
                photoUploadManager = ReflectUtils.reflect(PhotoUploadManager.class).create(OneOne.mContext).get();
                instancePool.put(PhotoUploadManager.class, photoUploadManager);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            photoUploadManager = (PhotoUploadManager) instancePool.get(PhotoUploadManager.class);
        }
        return photoUploadManager;
    }

    public NewTimeLineManager getNewTimeLineManager() {
        NewTimeLineManager newTimeLineManager = null;
        if (instancePool.get(NewTimeLineManager.class) == null) {
            try {
                newTimeLineManager = ReflectUtils.reflect(NewTimeLineManager.class).create(OneOne.mContext).get();
                instancePool.put(NewTimeLineManager.class, newTimeLineManager);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            newTimeLineManager = (NewTimeLineManager) instancePool.get(NewTimeLineManager.class);
        }
        return newTimeLineManager;
    }


}
