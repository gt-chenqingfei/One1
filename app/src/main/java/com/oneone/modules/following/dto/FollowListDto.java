package com.oneone.modules.following.dto;

import com.oneone.modules.following.beans.FollowListItem;

import java.util.List;

/**
 * Created by here on 18/4/25.
 */

public class FollowListDto {
    private int count;
    private List<FollowListItem> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FollowListItem> getList() {
        return list;
    }

    public void setList(List<FollowListItem> list) {
        this.list = list;
    }
}
