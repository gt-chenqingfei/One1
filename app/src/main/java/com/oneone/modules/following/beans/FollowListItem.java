package com.oneone.modules.following.beans;

import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;

/**
 * Created by here on 18/4/25.
 */

public class FollowListItem {
    private int followStatus;
    private long followedTime;
    private UserInfo userInfo;
    private int matchScore;
    private int intersectionCount;

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public long getFollowedTime() {
        return followedTime;
    }

    public void setFollowedTime(long followedTime) {
        this.followedTime = followedTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public int getIntersectionCount() {
        return intersectionCount;
    }

    public void setIntersectionCount(int intersectionCount) {
        this.intersectionCount = intersectionCount;
    }
}
