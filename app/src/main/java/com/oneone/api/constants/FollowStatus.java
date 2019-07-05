package com.oneone.api.constants;

/**
 * @author qingfei.chen
 * @since 2018/5/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface FollowStatus {
    /**
     * 互不关注
     */
    int STATUS_NO_FOLLOW = 0;
    /**
     * 我关注对方
     */
    int STATUS_FOLLOW_YOU = 1;
    /**
     * 对方关注我
     */
    int STATUS_FOLLOW_ME = 2;
    /**
     * 互相关注
     */
    int STATUS_FOLLOW_EACH = 3;
}
