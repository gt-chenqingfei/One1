package com.oneone.event;

import com.oneone.modules.timeline.bean.TimeLine;

/**
 * @author qingfei.chen
 * @since 2018/4/13.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class EventTimelineDelete {
    private boolean isClear = false;
    private TimeLine timeLine;
    private int position;

    public EventTimelineDelete(TimeLine timeLine, int position) {
        this.timeLine = timeLine;
        this.position = position;
    }

    public EventTimelineDelete(boolean isClear) {
        this.isClear = isClear;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public int getPosition() {
        return position;
    }

    public boolean isClear() {
        return isClear;
    }
}
