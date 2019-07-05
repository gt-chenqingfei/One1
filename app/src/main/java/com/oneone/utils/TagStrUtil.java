package com.oneone.utils;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/26.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TagStrUtil {
    public static String buildText(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < tags.size(); i++) {
            builder.append(tags.get(i));
            if (i < tags.size() - 1) {
                builder.append(" / ");
            }
        }

        return builder.toString();
    }
}
