package com.oneone.modules.timeline.bean;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLine4Moment extends AbstractTimeLine {
    private List<TimeLineImage> timelineImgs;

    public List<TimeLineImage> getTimelineImgs() {
        return timelineImgs;
    }

    public void setTimelineImgs(List<TimeLineImage> timelineImgs) {
        this.timelineImgs = timelineImgs;
    }
}
