package com.oneone.modules.user.bean;

public class UserAvatarInfo {

    private String userId;
    private String nickname;
    private String avatar;
    private String nicknamePending;
    private String avatarPending;
    private String wechatNickname;
    private String wechatAvatar;

    public UserAvatarInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNicknamePending() {
        return nicknamePending;
    }

    public void setNicknamePending(String nicknamePending) {
        this.nicknamePending = nicknamePending;
    }

    public String getAvatarPending() {
        return avatarPending;
    }

    public void setAvatarPending(String avatarPending) {
        this.avatarPending = avatarPending;
    }

    public String getWechatNickname() {
        return wechatNickname;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname;
    }

    public String getWechatAvatar() {
        return wechatAvatar;
    }

    public void setWechatAvatar(String wechatAvatar) {
        this.wechatAvatar = wechatAvatar;
    }
}
