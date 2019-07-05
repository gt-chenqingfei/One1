package com.oneone.modules.find.dto;

import java.util.List;

/**
 * Created by here on 18/5/3.
 */

public class LikeListDto {
    private List<LikeUserDto> likeList;
    private long preReadTime;
    private int count;

    public List<LikeUserDto> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeUserDto> likeList) {
        this.likeList = likeList;
    }

    public long getPreReadTime() {
        return preReadTime;
    }

    public void setPreReadTime(long preReadTime) {
        this.preReadTime = preReadTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
