package com.oneone.modules.msg.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.oneone.modules.msg.beans.TalkBeans.MyRecentContact;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserInfoBase;

/**
 * Created by here on 18/5/10.
 */

public class IMUserPrerelation implements Parcelable{
    private boolean needGift;
    private boolean relation;
    private String toImUserId;

    private UserInfo userInfo;

    private MyRecentContact recentContact;

    public MyRecentContact getMyRecentContact () {
        return recentContact;
    }

    public IMUserPrerelation () {

    }

    public IMUserPrerelation (boolean needGift, boolean relation, MyRecentContact recentContact) {
        this.needGift = needGift;
        this.relation = relation;
        this.recentContact = recentContact;
        if (recentContact != null) {
            this.toImUserId = recentContact.getMyTargetId();
            if (recentContact.getFirstRelation() != null)
                this.userInfo = recentContact.getFirstRelation().getUserInfo();
        }
    }

    protected IMUserPrerelation(Parcel in) {
        needGift = in.readByte() != 0;
        relation = in.readByte() != 0;
        toImUserId = in.readString();
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
    }

    public static final Creator<IMUserPrerelation> CREATOR = new Creator<IMUserPrerelation>() {
        @Override
        public IMUserPrerelation createFromParcel(Parcel in) {
            return new IMUserPrerelation(in);
        }

        @Override
        public IMUserPrerelation[] newArray(int size) {
            return new IMUserPrerelation[size];
        }
    };

    public boolean isNeedGift() {
        return needGift;
    }

    public void setNeedGift(boolean needGift) {
        this.needGift = needGift;
    }

    public boolean isRelation() {
        return relation;
    }

    public void setRelation(boolean relation) {
        this.relation = relation;
    }

    public String getToImUserId() {
        return toImUserId;
    }

    public void setToImUserId(String toImUserId) {
        this.toImUserId = toImUserId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (needGift ? 1 : 0));
        parcel.writeByte((byte) (relation ? 1 : 0));
        parcel.writeString(toImUserId);
        parcel.writeParcelable(userInfo, i);
    }

    @Override
    public String toString() {
        return "IMUserPrerelation{" +
                "needGift=" + needGift +
                ", relation=" + relation +
                ", toImUserId='" + toImUserId + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
