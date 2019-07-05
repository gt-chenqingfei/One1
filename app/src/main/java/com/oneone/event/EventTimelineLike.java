package com.oneone.event;

import com.oneone.modules.timeline.bean.TimeLine;

/**
 * @author qingfei.chen
 * @since 2018/4/13.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class EventTimelineLike {
    private TimeLine timeLine;
    private int position;

    public EventTimelineLike(TimeLine timeLine, int position) {
        this.timeLine = timeLine;
        this.position = position;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public int getPosition() {
        return position;
    }

}
