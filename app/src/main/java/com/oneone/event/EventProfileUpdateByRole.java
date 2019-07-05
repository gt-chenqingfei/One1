package com.oneone.event;

import com.oneone.modules.user.bean.UserProfileUpdateBean;

/**
 * @author qingfei.chen
 * @since 2018/4/13.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class EventProfileUpdateByRole {
    UserProfileUpdateBean userInfo;

    public EventProfileUpdateByRole(UserProfileUpdateBean userInfo) {
        this.userInfo = userInfo;
    }

    public UserProfileUpdateBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserProfileUpdateBean userInfo) {
        this.userInfo = userInfo;
    }
}
