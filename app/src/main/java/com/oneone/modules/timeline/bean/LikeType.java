package com.oneone.modules.timeline.bean;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class LikeType {
    private int likeType;
    private int likeTypeCount;

    public LikeType(int likeType, int likeTypeCount) {
        this.likeType = likeType;
        this.likeTypeCount = likeTypeCount;
    }

    public int getLikeType() {
        return likeType;
    }

    public void setLikeType(int likeType) {
        this.likeType = likeType;
    }

    public int getLikeTypeCount() {
        return likeTypeCount;
    }

    public void setLikeTypeCount(int likeTypeCount) {
        this.likeTypeCount = likeTypeCount;
    }

    @Override
    public String toString() {
        return "LikeType{" +
                "likeType=" + likeType +
                ", likeTypeCount=" + likeTypeCount +
                '}';
    }
}
