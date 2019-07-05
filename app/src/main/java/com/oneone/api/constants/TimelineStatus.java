package com.oneone.api.constants;

/**
 * @author qingfei.chen
 * @since 2018/4/12.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface TimelineStatus {

    /**
     * 审核未通过
     */
    int STATUS_NOT_APPROVED = -2;
    /**
     * 正常
     */
    int STATUS_OK = 0;
    /**
     * 正在发送
     */
    int STATUS_SENDING = -5;
    /**
     * 发送失败
     */
    int STATUS_SEND_FAILED = -6;
}
