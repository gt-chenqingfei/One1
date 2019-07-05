package com.oneone.framework.ui.imagepicker;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oneone.framework.ui.imagepicker.bean.ImageItem;


/**
 * 新的DataHolder，使用单例和弱引用解决崩溃问题
 * <p>
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-20  07:01
 */
public class DataHolder {
    public static final String DH_CURRENT_IMAGE_FOLDER_ITEMS = "dh_current_image_folder_items";

    private static DataHolder mInstance;
    private Map<String, List<ImageItem>> data;

    public static DataHolder getInstance() {
        if (mInstance == null) {
            synchronized (DataHolder.class) {
                if (mInstance == null) {
                    mInstance = new DataHolder();
                }
            }
        }
        return mInstance;
    }

    private DataHolder() {
        data = new HashMap<>();
    }

    public void save(String id, List<ImageItem> object) {
        if (data != null) {
            data.put(id, object);
        }
    }

    public Object retrieve(String id) {
        if (data == null || mInstance == null) {
            throw new RuntimeException("You must init!");
        }
        return data.get(id);
    }
}