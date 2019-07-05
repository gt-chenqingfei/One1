package com.oneone.modules.msg.beans;

import android.support.annotation.NonNull;

import com.oneone.modules.user.bean.UserInfo;

/**
 * Created by here on 18/6/14.
 */

public class NotifyListItem implements Comparable<NotifyListItem> {
    private int unread;
    private int notifyType;
    private long notifyTime;
    private UserInfo fromUserInfo;
    private NotifyBody notifyBody;

    private TimeLineInfo timeLineInfo;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public UserInfo getFromUserInfo() {
        return fromUserInfo;
    }

    public void setFromUserInfo(UserInfo fromUserInfo) {
        this.fromUserInfo = fromUserInfo;
    }

    public NotifyBody getNotifyBody() {
        return notifyBody;
    }

    public void setNotifyBody(NotifyBody notifyBody) {
        this.notifyBody = notifyBody;
    }

    public TimeLineInfo getTimeLineInfo() {
        return timeLineInfo;
    }

    public void setTimeLineInfo(TimeLineInfo timeLineInfo) {
        this.timeLineInfo = timeLineInfo;
    }

    @Override
    public int compareTo(@NonNull NotifyListItem notifyListItem) {
        if (notifyTime > notifyListItem.getNotifyTime())
            return -1;
        else if (notifyTime < notifyListItem.getNotifyTime())
            return 1;
        return 0;
    }
}
