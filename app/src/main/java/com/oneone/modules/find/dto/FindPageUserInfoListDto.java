package com.oneone.modules.find.dto;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public class FindPageUserInfoListDto {
    private List<FindPageUserInfoDTO> userList;
    private int expire;
    private int recommendSize;

    public List<FindPageUserInfoDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<FindPageUserInfoDTO> userList) {
        this.userList = userList;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getRecommendSize() {
        return recommendSize;
    }

    public void setRecommendSize(int recommendSize) {
        this.recommendSize = recommendSize;
    }
}
