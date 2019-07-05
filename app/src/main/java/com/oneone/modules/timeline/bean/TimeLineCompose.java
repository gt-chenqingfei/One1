package com.oneone.modules.timeline.bean;

import com.oneone.modules.user.bean.UserInfoBase;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineCompose {
    /**
     * 普通动态文字内容，type==moment时显示
     */
    private String content;
    /**
     * 普通动态图片，type == moment时显示
     */
    private List<TimeLineImage> timelineImgs;
    /**
     * 新加入的单身信息，type = newsingle时显示
     */
    private UserInfoBase userInfo;

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

    public UserInfoBase getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBase userInfo) {
        this.userInfo = userInfo;
    }
}
