package com.oneone.modules.user.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @author qingfei.chen
 * @since 2018/4/19.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class UserInfoBase implements Cloneable, Parcelable {
    private int role;
    private String userId;
    private String nickname;
    private String nicknamePending;
    private String avatar;
    private String avatarPending;
    private int sex;
    private int age;
    private String province;
    private String occupation;
    private String city;
    private String wechatNickname;
    private String wechatAvatar;
    private String company;
    private String industry;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        if (TextUtils.isEmpty(nickname)) {
            if (!TextUtils.isEmpty(userId)) {
                return "用户" + userId.substring(0, 4);
            } else {
                return "";
            }
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNicknamePending() {
        return nicknamePending;
    }

    public void setNicknamePending(String nicknamePending) {
        this.nicknamePending = nicknamePending;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public String getDiscoverPlace () {
        if (province.equals("北京") || province.equals("天津") || province.equals("上海") || province.equals("重庆"))
            return province;
        else {
            return province + " " + city;
        }
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarPending() {
        return avatarPending;
    }

    public void setAvatarPending(String avatarPending) {
        this.avatarPending = avatarPending;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMyNickname() {
//        if (!TextUtils.isEmpty(nicknamePending)) {
//            return nicknamePending;
//        } else {
//        }
        return getNickname();
    }

    public String getMyAvatar() {
        if (!TextUtils.isEmpty(avatarPending)) {
            return avatarPending;
        } else {
            return avatar;
        }
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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

    @Override
    protected UserInfoBase clone() throws CloneNotSupportedException {
        return (UserInfoBase) super.clone();
    }

    public static final Creator<UserInfoBase> CREATOR = new Creator<UserInfoBase>() {
        @Override
        public UserInfoBase createFromParcel(Parcel source) {
            //从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层。
            UserInfoBase userInfoBase = new UserInfoBase();
            userInfoBase.setRole(source.readInt());
            userInfoBase.setUserId(source.readString());
            userInfoBase.setNickname(source.readString());
            userInfoBase.setNicknamePending(source.readString());
            userInfoBase.setAvatar(source.readString());
            userInfoBase.setAvatarPending(source.readString());
            userInfoBase.setSex(source.readInt());
            userInfoBase.setAge(source.readInt());
            userInfoBase.setProvince(source.readString());
            userInfoBase.setOccupation(source.readString());
            userInfoBase.setCity(source.readString());
            userInfoBase.setWechatNickname(source.readString());
            userInfoBase.setWechatAvatar(source.readString());
            userInfoBase.setCompany(source.readString());
            userInfoBase.setIndustry(source.readString());
            return userInfoBase;
        }

        @Override
        public UserInfoBase[] newArray(int size) {
            //创建一个类型为T，长度为size的数组，仅一句话（return new T[size])即可。方法是供外部类反序列化本类数组使用。
            return new UserInfoBase[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.role);
        dest.writeString(this.userId);
        dest.writeString(this.nickname);
        dest.writeString(this.nicknamePending);
        dest.writeString(this.avatar);
        dest.writeString(this.avatarPending);
        dest.writeInt(this.sex);
        dest.writeInt(this.age);
        dest.writeString(this.province);
        dest.writeString(this.occupation);
        dest.writeString(this.city);
        dest.writeString(this.wechatNickname);
        dest.writeString(this.wechatAvatar);
        dest.writeString(this.company);
        dest.writeString(this.industry);
    }

    public UserInfoBase() {
    }

    protected UserInfoBase(Parcel in) {
        this.role = in.readInt();
        this.userId = in.readString();
        this.nickname = in.readString();
        this.nicknamePending = in.readString();
        this.avatar = in.readString();
        this.avatarPending = in.readString();
        this.sex = in.readInt();
        this.age = in.readInt();
        this.province = in.readString();
        this.occupation = in.readString();
        this.city = in.readString();
        this.wechatNickname = in.readString();
        this.wechatAvatar = in.readString();
        this.company = in.readString();
        this.industry = in.readString();
    }

}
