package com.oneone.modules.matcher.relations.dto;

/**
 * @author qingfei.chen
 * @since 2018/4/19.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MatcherCountDto {
    private int newMatcherCount;
    private int totalMatcherCount;

    public int getNewMatcherCount() {
        return newMatcherCount;
    }

    public void setNewMatcherCount(int newMatcherCount) {
        this.newMatcherCount = newMatcherCount;
    }

    public int getTotalMatcherCount() {
        return totalMatcherCount;
    }

    public void setTotalMatcherCount(int totalMatcherCount) {
        this.totalMatcherCount = totalMatcherCount;
    }
}
