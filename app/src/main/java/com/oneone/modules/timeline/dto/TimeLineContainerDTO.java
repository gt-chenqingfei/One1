package com.oneone.modules.timeline.dto;

import com.oneone.modules.timeline.bean.TimeLineTopic;
import com.oneone.modules.timeline.bean.TimeLine;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineContainerDTO {
    private int count;
    private List<TimeLine> list;
    private TimeLineTopic operationTagInfo;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TimeLine> getList() {
        return list;
    }

    public void setList(List<TimeLine> list) {
        this.list = list;
    }

    public TimeLineTopic getOperationTagInfo() {
        return operationTagInfo;
    }

}
