package com.oneone.modules.msg.beans;

/**
 * Created by here on 18/6/14.
 */

public class TimeLineInfo {
    private int timelineId;
    private String userId;
    private TimeLineInfoDetail detail;

    public int getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(int timelineId) {
        this.timelineId = timelineId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TimeLineInfoDetail getDetail() {
        return detail;
    }

    public void setDetail(TimeLineInfoDetail detail) {
        this.detail = detail;
    }
}
