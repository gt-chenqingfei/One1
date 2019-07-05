package com.oneone.modules.msg.dto;

import com.oneone.modules.msg.beans.NotifyListItem;
import com.oneone.modules.msg.beans.TimeLineInfo;

import java.util.List;

/**
 * Created by here on 18/6/14.
 */

public class NotifyListDto {
    private int count;
    private List<TimeLineInfo> timelineList;
    private long lastReadTime;
    private List<NotifyListItem> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TimeLineInfo> getTimelineList() {
        return timelineList;
    }

    public void setTimelineList(List<TimeLineInfo> timelineList) {
        this.timelineList = timelineList;
    }

    public List<NotifyListItem> getList() {
        return list;
    }

    public void setList(List<NotifyListItem> list) {
        this.list = list;
    }

    public long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
