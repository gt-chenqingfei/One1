package com.oneone.modules.entry.beans;

public class LoginInfo implements Cloneable {
    private String userId;
    private boolean alreadyBindWechat; // 如果是手机号登陆，代表是否已绑定微信账号，如果是微信登陆，代表是否绑定手机号
    private String jwtToken;
    private int accountRole; // 用户身份 0-未开启身份 1-双身份 2-纯Matcher
    private int accountStatus; // 账号状态 1-正常（暂时只有正常状态，后期根据业务需要定义多种状态）
    private String accountSign;
    private int alreadyRegistered;

    public String getAccountSign() {
        return accountSign;
    }

    public void setAccountSign(String accountSign) {
        this.accountSign = accountSign;
    }

    public int getAlreadyRegistered() {
        return alreadyRegistered;
    }

    public void setAlreadyRegistered(int alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAlreadyBindWechat() {
        return alreadyBindWechat;
    }

    public void setAlreadyBindWechat(boolean alreadyBindWechat) {
        this.alreadyBindWechat = alreadyBindWechat;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public int getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(int accountRole) {
        this.accountRole = accountRole;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return "LoginInfo [userId=" + userId + ", alreadyBindWechat=" + alreadyBindWechat + ", jwtToken="
                + jwtToken + ", accountRole=" + accountRole + ", accountStatus=" + accountStatus + "]";
    }

    @Override
    public LoginInfo clone() {
        try {
            return (LoginInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
