package com.oneone.modules.user;

import com.google.gson.Gson;
import com.oneone.OneOne;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.user.bean.UserInfo;

/**
 * @author qingfei.chen
 * @since 2018/4/11.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class HereUser {
    private static final String SP_KEY = HereUser.class.getSimpleName();
    private String token;
    private LoginInfo loginInfo;
    private static HereUser instance;
    private UserInfo userInfo;
    private String userId;
    private IMUser imUserInfo;

    private HereUser() {
        userInfo = new UserInfo();
    }

    private HereUser(String token) {
        this();
        setToken(token);
    }

    public static HereUser getInstance() {
        if (instance == null) {
            String json = DefaultSP.getInstance()
                    .getString(OneOne.mContext, SP_KEY, null);
            if (json != null) {
                instance = new Gson().fromJson(json, HereUser.class);
            }
        }
        return instance;
    }

    public static HereUser newHereUser(LoginInfo info) {
        if (info == null) {
            throw new IllegalArgumentException("New HereUser instance  info can not be  null!");
        }

        instance = new HereUser(info.getJwtToken());
        instance.setLoginInfo(info);
        String json = new Gson().toJson(instance);
        DefaultSP.getInstance().put(OneOne.mContext, SP_KEY, json).commit();
        return instance;
    }

    public static void removeHereUser() {
        DefaultSP.getInstance().remove(OneOne.mContext, SP_KEY).commit();
        instance = null;
    }

    public void updateUserInfo(LoginInfo info) {
        setLoginInfo(info);
        saveUser();
    }

    public void updateUserInfo(UserInfo info) {
        if (null == info) {
            return;
        }

        setUserInfo(info);
        saveUser();
    }

    private void saveUser() {
        String json = new Gson().toJson(instance);
        DefaultSP.getInstance().put(OneOne.mContext, SP_KEY, json).commit();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        try {
            this.userInfo = userInfo.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public static String getUserId() {
        if (instance == null) {
            return "";
        }
        if (instance.userInfo == null) {
            return "";
        }
        return instance.userInfo.getUserId();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public IMUser getImUserInfo() {
        return imUserInfo;
    }

    public void setImUserInfo(IMUser imUserInfo) {
        this.imUserInfo = imUserInfo;
    }
}
