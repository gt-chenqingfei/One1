package com.oneone.modules.matcher.relations.dto;

import com.oneone.modules.matcher.relations.bean.MatcherInfo;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/18.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MyMatchersDto {
    private int count;
    private List<MatcherInfo> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MatcherInfo> getList() {
        return list;
    }

    public void setList(List<MatcherInfo> list) {
        this.list = list;
    }
}
