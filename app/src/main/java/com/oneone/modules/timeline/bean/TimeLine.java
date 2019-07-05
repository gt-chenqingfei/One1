package com.oneone.modules.timeline.bean;

import com.oneone.modules.user.bean.UserInfoBase;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLine {
    private long timelineId;
    private UserInfoBase userInfo;
    private long postTime;
    private List<LikeType> likeTypes;
    private int myLikeType;
    private int status;
    private TimeLineDetail detail;

    public long getTimelineId() {
        return timelineId;
    }

    public void setTimelineId(long timelineId) {
        this.timelineId = timelineId;
    }

    public UserInfoBase getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBase userInfo) {
        this.userInfo = userInfo;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public List<LikeType> getLikeTypes() {
        return likeTypes;
    }

    public void setLikeTypes(List<LikeType> likeTypes) {
        this.likeTypes = likeTypes;
    }

    public int getMyLikeType() {
        return myLikeType;
    }

    public void setMyLikeType(int myLikeType) {
        this.myLikeType = myLikeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TimeLineDetail getDetail() {
        return detail;
    }

    public void setDetail(TimeLineDetail detail) {
        this.detail = detail;
    }

}
