package com.oneone.api.constants;

/**
 * @author qingfei.chen
 * @since 2018/4/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface Role {
    /**
     * 未开启用户身份，未注册用户
     */
    int UNKNOWN = 0;
    /**
     * 双身份 已注册用户
     */
    int SINGLE = 1;
    /**
     * 纯Matcher 已注册用户
     */
    int MATCHER = 2;
}
