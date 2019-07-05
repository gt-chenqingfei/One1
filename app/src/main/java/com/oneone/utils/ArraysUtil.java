package com.oneone.utils;

import android.content.Context;

/**
 * 数组解析类
 * <p>
 * Created by ZhaiDongyang on 2018/7/18
 */
public class ArraysUtil {

    /**
     * 例如 <item>不吸烟，且反感吸烟,1</item> 通过 key 1 前面的值
     *
     * @param context
     * @param arrays
     * @param key
     * @return
     */
    public static String getValueByKey(Context context, int arrays, int key) {
        String[] stringArray = context.getResources().getStringArray(arrays);
        for (String string : stringArray) {
            String[] strings = string.split(",");
            String stringsKey = strings[1]; // 不管 array 数组里面的每条数据是几个值，第二个值都是 key
            if (Integer.parseInt(stringsKey) == key) {
                String value = strings[0];
                return value;
            }
        }
        return null;
    }
}
