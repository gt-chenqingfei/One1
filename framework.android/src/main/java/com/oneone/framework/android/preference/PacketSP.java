package com.oneone.framework.android.preference;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by chenqingfei on 16/8/16.
 */
public class PacketSP {

    private PacketSP() {
    }

    private static PacketSP instance = new PacketSP();

    public static PacketSP getInstance() {
        return instance;
    }

    /**
     * 存储用户的配置信息
     */
    private SharedPreferences packetSp = null;

    /**
     * 获取 packetSp的实例对象
     *
     * @return
     */
    private SharedPreferences getPacketSp(Context context) {
        if (packetSp == null) {
            packetSp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
        return packetSp;
    }

    /**
     * 注册 PacketSP OnSharedPreferenceChangeListener
     *
     * @param context
     * @param listener
     */
    public void registerListener(Context context,
                                 SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getPacketSp(context);
        sp.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * 注销 PacketSP
     *
     * @param context
     * @param listener
     */
    public void unRegisterListener(Context context,
                                   SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences sp = getPacketSp(context);
        sp.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public SharedPreferences.Editor put(Context context, String key, Object object) {

        SharedPreferences sp = getPacketSp(context);
        SharedPreferences.Editor editor = sp.edit();

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

        return editor;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = getPacketSp(context);

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

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public SharedPreferences.Editor remove(Context context, String key) {
        SharedPreferences sp = getPacketSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor;
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public SharedPreferences.Editor clear(Context context) {
        SharedPreferences sp = getPacketSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor;
    }
}
