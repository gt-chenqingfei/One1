package com.oneone.modules.find.dto;

import java.util.List;

/**
 * Created by here on 18/5/3.
 */

public class LikeUnreadListDto {
    private List<LikeUserDto> newLikeEachOtherUserList;
    private int likeToMeCount;

    public List<LikeUserDto> getNewLikeEachOtherUserList() {
        return newLikeEachOtherUserList;
    }

    public void setNewLikeEachOtherUserList(List<LikeUserDto> newLikeEachOtherUserList) {
        this.newLikeEachOtherUserList = newLikeEachOtherUserList;
    }

    public int getLikeToMeCount() {
        return likeToMeCount;
    }

    public void setLikeToMeCount(int likeToMeCount) {
        this.likeToMeCount = likeToMeCount;
    }
}
