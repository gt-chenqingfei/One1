package com.oneone.modules.matcher.relations.bean;

/**
 * @author qingfei.chen
 * @since 2018/4/19.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MatcherInfo extends SingleInfo {
    private int accessType;
    private String relationCreatedTime;

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    public String getRelationCreatedTime() {
        return relationCreatedTime;
    }

    public void setRelationCreatedTime(String relationCreatedTime) {
        this.relationCreatedTime = relationCreatedTime;
    }
}
