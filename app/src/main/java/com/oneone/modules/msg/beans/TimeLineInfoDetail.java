package com.oneone.modules.msg.beans;

/**
 * Created by here on 18/6/14.
 */

public class TimeLineInfoDetail {
    private String type;
    private TimeLineInfoInnerDetail detail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TimeLineInfoInnerDetail getDetail() {
        return detail;
    }

    public void setDetail(TimeLineInfoInnerDetail detail) {
        this.detail = detail;
    }
}
