package com.oneone.modules.matcher.relations.bean;

import com.oneone.modules.user.bean.UserInfoBase;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/18.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class SingleInfo {
    private UserInfoBase userInfo;
    private String relationship;
    private String matcherSaid;
    private String matcherSaidTime;
    private int matcherSaidFlg;
    private int newRelationFlg;
    private int intersectionCount;
    private int matchScore;
    private List<ImageInfo> imgs;

    public UserInfoBase getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBase userInfo) {
        this.userInfo = userInfo;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getMatcherSaid() {
        return matcherSaid;
    }

    public void setMatcherSaid(String matcherSaid) {
        this.matcherSaid = matcherSaid;
    }

    public int getMatcherSaidFlg() {
        return matcherSaidFlg;
    }

    public void setMatcherSaidFlg(int matcherSaidFlg) {
        this.matcherSaidFlg = matcherSaidFlg;
    }

    public int getNewRelationFlg() {
        return newRelationFlg;
    }

    public void setNewRelationFlg(int newRelationFlg) {
        this.newRelationFlg = newRelationFlg;
    }

    public String getMatcherSaidTime() {
        return matcherSaidTime;
    }

    public void setMatcherSaidTime(String matcherSaidTime) {
        this.matcherSaidTime = matcherSaidTime;
    }

    public List<ImageInfo> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImageInfo> imgs) {
        this.imgs = imgs;
    }

    public int getIntersectionCount() {
        return intersectionCount;
    }

    public void setIntersectionCount(int intersectionCount) {
        this.intersectionCount = intersectionCount;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }
}
