package com.oneone.modules.find.dto;

import android.support.annotation.NonNull;

import com.oneone.modules.user.bean.UserInfo;

/**
 * Created by here on 18/5/3.
 */

public class LikeUserDto implements Comparable<LikeUserDto> {
    private UserInfo userInfo;
    private int likeStatus;
    private long likeTime;
    private int matchValue;
    private int intersectionValue;
    private int unread;

    private boolean isNoMore = false;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public long getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(long likeTime) {
        this.likeTime = likeTime;
    }

    public int getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(int matchValue) {
        this.matchValue = matchValue;
    }

    public int getIntersectionValue() {
        return intersectionValue;
    }

    public void setIntersectionValue(int intersectionValue) {
        this.intersectionValue = intersectionValue;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public boolean isNoMore() {
        return isNoMore;
    }

    public void setNoMore(boolean noMore) {
        isNoMore = noMore;
    }

    @Override
    public int compareTo(@NonNull LikeUserDto likeUserDto) {
        if (likeTime > likeUserDto.getLikeTime())
            return -1;
        else if (likeTime < likeUserDto.getLikeTime())
            return 1;
        return 0;
    }
}
