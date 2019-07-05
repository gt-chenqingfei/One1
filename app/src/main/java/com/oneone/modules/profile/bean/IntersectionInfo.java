package com.oneone.modules.profile.bean;

import com.oneone.modules.user.bean.UserAvatarInfo;

import java.util.List;

public class IntersectionInfo {

    public IntersectionInfo() {
    }

    private int matcherCount;
    private List<UserAvatarInfo> matcherList;
    private int friendCount;
    private List<UserAvatarInfo> friendList;
    private int tagCount;
    private List<String> tagList;

    public int getMatcherCount() {
        return matcherCount;
    }

    public void setMatcherCount(int matcherCount) {
        this.matcherCount = matcherCount;
    }

    public List<UserAvatarInfo> getMatcherList() {
        return matcherList;
    }

    public void setMatcherList(List<UserAvatarInfo> matcherList) {
        this.matcherList = matcherList;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public List<UserAvatarInfo> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<UserAvatarInfo> friendList) {
        this.friendList = friendList;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
