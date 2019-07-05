package com.oneone.modules.timeline.dto;

import com.oneone.modules.timeline.bean.TimeLineTopic;

import java.util.List;

/**
 * 话题搜索返回数据
 *
 * Created by ZhaiDongyang on 2018/6/21
 */
public class TimeLineTopicSearchDTO {
    private int count;
    private List<TimeLineTopic> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TimeLineTopic> getList() {
        return list;
    }

    public void setList(List<TimeLineTopic> list) {
        this.list = list;
    }
}
