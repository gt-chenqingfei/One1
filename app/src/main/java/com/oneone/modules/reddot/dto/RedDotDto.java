package com.oneone.modules.reddot.dto;

/**
 * @author qingfei.chen
 * @since 2018/7/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class RedDotDto {
    private int unreadCount;
    private long lastReadTime;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
