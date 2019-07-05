package com.oneone.modules.matcher.relations.dto;


import com.oneone.modules.matcher.relations.bean.SingleInfo;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/18.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class MySinglesDto {
    private int count;
    private List<SingleInfo> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SingleInfo> getList() {
        return list;
    }

    public void setList(List<SingleInfo> list) {
        this.list = list;
    }
}
