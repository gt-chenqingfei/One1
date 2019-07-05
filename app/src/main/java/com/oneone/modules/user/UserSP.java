package com.oneone.modules.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.modules.user.bean.UserInfo;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author qingfei.chen
 * @since 2018/4/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class UserSP {
    public static final String IM_USER_ID = "IM_USER_ID_";
    public static final String IM_USER_TOKEN = "IM_USER_TOKEN_";

    public static final String IM_USER_RELATION = "IM_USER_RELATION_";
    public static final String DOG_FOOT_BALANCE = "DOG_FOOT_BALANCE";

    public static final String LIKE_RELATION_MY_LIKE = "LIKE_RELATION_MY_LIKE_";
    public static final String LIKE_RELATION_LIKE_ME = "LIKE_RELATION_LIKE_ME_";
    public static final String LIKE_RELATION_LIKE_EACHOTHER = "LIKE_RELATION_LIKE_EACHOTHER_";

    private Map<Context, SharedPreferences.OnSharedPreferenceChangeListener> listeners = new HashMap<>();

    private UserSP() {
    }

    private static UserSP instance = new UserSP();

    public static UserSP getInstance() {
        return instance;
    }

    /**
     * 存储用户的配置信息
     */
    private SharedPreferences userSp = null;

    /**
     * 获取 userSp的实例对象
     *
     * @return
     */
    private SharedPreferences getUserSp(Context context) {
        if (userSp == null) {
            if (HereUser.getInstance() == null) {
                LoggerFactory.getLogger("HereUser").error("HereUser.getInstance() is not instantiated");
                return null;
            }
            LoginInfo user = HereUser.getInstance().getLoginInfo();
            if (user == null) {
                throw new RuntimeException("User is null, cannot be instantiated");
            }
            userSp = context.getSharedPreferences(user.getUserId(), Context.MODE_PRIVATE);
        }
        return userSp;
    }

    public static void destory() {
        if (instance == null) {
            return;
        }
        Iterator it = instance.listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Context key = (Context) entry.getKey();
            SharedPreferences.OnSharedPreferenceChangeListener value = (SharedPreferences.OnSharedPreferenceChangeListener) entry.getValue();
            instance.unregisterListener(key, value);
        }
        instance.userSp = null;
    }

    /**
     * 注册 UserSP OnSharedPreferenceChangeListener
     *
     * @param context
     * @param listener
     */
    public void registerListener(Context context,
                                 SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getUserSp(context);
        if (sp == null) {
            return;
        }
        sp.registerOnSharedPreferenceChangeListener(listener);
        listeners.put(context, listener);
    }

    /**
     * 注销 UserSP OnSharedPreferenceChangeListener
     *
     * @param context
     * @param listener
     */
    public void unregisterListener(Context context,
                                   SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getUserSp(context);
        if (sp == null) {
            return;
        }
        sp.unregisterOnSharedPreferenceChangeListener(listener);
        listeners.remove(context);
    }

    /**
     * 获取 Editor
     *
     * @param context
     * @return
     */
    public SharedPreferences.Editor getEditor(Context context) {
        userSp = getUserSp(context);
        return userSp.edit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void putAndApply(Context context, String key, Object object) {
        if (instance == null) {
            return;
        }
        instance.userSp = instance.getUserSp(context);
        if (instance.userSp == null) {
            return;
        }
        SharedPreferences.Editor editor = instance.userSp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        editor.apply();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void putAndCommit(Context context, String key, Object object) {
        if (instance == null) {
            return;
        }
        instance.userSp = instance.getUserSp(context);
        if (instance.userSp == null) {
            return;
        }
        SharedPreferences.Editor editor = instance.userSp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        if (instance == null) {
            return defaultObject;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return null;
        }
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return defaultObject;
    }

    public static int getInt(Context context, String key, int defaultValue) {
        if (instance == null) {
            return defaultValue;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return defaultValue;
        }
        return sp.getInt(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        if (instance == null) {
            return defaultValue;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return defaultValue;
        }
        return sp.getString(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (instance == null) {
            return defaultValue;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return defaultValue;
        }
        return sp.getBoolean(key, defaultValue);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        if (instance == null) {
            return defaultValue;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return defaultValue;
        }

        return sp.getFloat(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        if (instance == null) {
            return defaultValue;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return defaultValue;
        }
        return sp.getLong(key, defaultValue);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        if (instance == null) {
            return;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        if (instance == null) {
            return;
        }
        SharedPreferences sp = instance.getUserSp(context);
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();

        editor.clear().commit();
    }


}
