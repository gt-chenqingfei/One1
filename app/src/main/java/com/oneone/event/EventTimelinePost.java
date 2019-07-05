package com.oneone.event;

import com.oneone.modules.timeline.bean.TimeLine;

/**
 * @author qingfei.chen
 * @since 2018/4/13.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class EventTimelinePost {
    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SEND_SUCCESS= 1;
    public static final int STATUS_SEND_EXCEPTION = 2;
    private int status = STATUS_SENDING;

    public EventTimelinePost(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
