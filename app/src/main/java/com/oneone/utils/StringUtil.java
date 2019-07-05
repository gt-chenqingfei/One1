package com.oneone.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by chenqingfei on 15/12/10.
 */
public class StringUtil {
    /**
     * 获取字符数，英文只占半个
     *
     * @param str
     * @return
     */
    public static int getLength(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return 0;
        }
        try {
            int utfLength = str.getBytes("UTF-8").length;
            String s = new String(str.getBytes(), "UTF-8");
            int length = s.length();
            int chNum = (utfLength - length) / 2;
            int lastLength = (length - chNum + 1) / 2 + chNum;
            return lastLength;
        } catch (UnsupportedEncodingException e1) {
        }
        return 0;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }
}
