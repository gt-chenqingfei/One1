package com.oneone.api.request;

import com.oneone.modules.timeline.bean.TimeLineImage;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/6/26.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimelinePostDto {
    private String content;
    private List<TimeLineImage> timelineImgs;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TimeLineImage> getTimelineImgs() {
        return timelineImgs;
    }

    public void setTimelineImgs(List<TimeLineImage> timelineImgs) {
        this.timelineImgs = timelineImgs;
    }
}
