package com.oneone.modules.timeline.bean;

import com.oneone.modules.user.bean.UserInfoBase;

/**
 * @author qingfei.chen
 * @since 2018/5/30.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLine4NewSingle extends AbstractTimeLine {
    UserInfoBase userInfo;

    public UserInfoBase getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBase userInfo) {
        this.userInfo = userInfo;
    }
}
