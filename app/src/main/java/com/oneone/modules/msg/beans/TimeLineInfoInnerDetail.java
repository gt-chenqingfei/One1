package com.oneone.modules.msg.beans;

import java.util.List;

/**
 * Created by here on 18/6/14.
 */

public class TimeLineInfoInnerDetail {
    private String content;
    private List<TimeLineImgs> timelineImgs;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TimeLineImgs> getTimelineImgs() {
        return timelineImgs;
    }

    public void setTimelineImgs(List<TimeLineImgs> timelineImgs) {
        this.timelineImgs = timelineImgs;
    }
}
