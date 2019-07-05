package com.oneone.modules.user.dto;

import com.oneone.modules.entry.beans.RelationInfo;
import com.oneone.modules.user.bean.QaAnswer;
import com.oneone.modules.user.bean.UserInfo;

public class UserInfoDTO {
    private UserInfo userInfo;
    private RelationInfo relationInfo;
    private float matchValue;
    private int intersectionValue;
    private int follow;
    private QaAnswer qaAnswer;

    public UserInfoDTO() {
        userInfo = new UserInfo();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getIntersectionValue() {
        return intersectionValue;
    }

    public void setIntersectionValue(int intersectionValue) {
        this.intersectionValue = intersectionValue;
    }

    public float getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(float matchValue) {
        this.matchValue = matchValue;
    }

    public RelationInfo getRelationInfo() {
        return relationInfo;
    }

    public void setRelationInfo(RelationInfo relationInfo) {
        this.relationInfo = relationInfo;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {

        this.follow = follow;
    }

    public QaAnswer getQaAnswer() {
        return qaAnswer;
    }

    public void setQaAnswer(QaAnswer qaAnswer) {
        this.qaAnswer = qaAnswer;
    }
}
