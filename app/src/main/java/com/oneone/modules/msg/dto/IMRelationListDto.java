package com.oneone.modules.msg.dto;

import com.oneone.modules.msg.beans.IMFirstRelation;

import java.util.List;

/**
 * Created by here on 18/5/10.
 */

public class IMRelationListDto {
    private int oldCount = 0;
    private int newCount = 0;
    private int count = 0;
    private List<IMFirstRelation> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<IMFirstRelation> getList() {
        return list;
    }

    public void setList(List<IMFirstRelation> list) {
        this.list = list;
    }

    public int getOldCount() {
        return oldCount;
    }

    public void setOldCount(int oldCount) {
        this.oldCount = oldCount;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }
}
