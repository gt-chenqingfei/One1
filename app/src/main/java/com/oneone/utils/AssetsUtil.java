package com.oneone.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把 Assets 中的文件内容转化成 String
 *
 * Created by ZhaiDongyang on 2018/7/10
 */
public class AssetsUtil {

    public static String getContentFromAssets(Context context, String fileName) {
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String content = new String(buffer, "UTF-8");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
