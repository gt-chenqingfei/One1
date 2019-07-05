package com.oneone.modules.find.dto;

import com.oneone.modules.entry.beans.RelationInfo;
import com.oneone.modules.find.beans.MatcherRecommendList;
import com.oneone.modules.find.beans.QaAnswer;
import com.oneone.modules.user.bean.UserInfo;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public class FindPageUserInfoDTO {
    private UserInfo userInfo;

    private RelationInfo relationInfo;
    private List<MatcherRecommendList> matcherRecommendList;
    private float matchValue;
    private int intersectionValue;
    private QaAnswer qaAnswer;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {

        this.userInfo = userInfo;
    }


    public RelationInfo getRelationInfo() {
        return relationInfo;
    }

    public void setRelationInfo(RelationInfo relationInfo) {
        this.relationInfo = relationInfo;
    }

    public List<MatcherRecommendList> getMatcherRecommendList() {
        return matcherRecommendList;
    }

    public void setMatcherRecommendList(List<MatcherRecommendList> matcherRecommendList) {
        this.matcherRecommendList = matcherRecommendList;
    }

    public float getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(float matchValue) {
        this.matchValue = matchValue;
    }

    public int getIntersectionValue() {
        return intersectionValue;
    }

    public void setIntersectionValue(int intersectionValue) {
        this.intersectionValue = intersectionValue;
    }

    public QaAnswer getQaAnswer() {
        return qaAnswer;
    }

    public void setQaAnswer(QaAnswer qaAnswer) {
        this.qaAnswer = qaAnswer;
    }
}
