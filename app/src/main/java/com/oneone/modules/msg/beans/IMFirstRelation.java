package com.oneone.modules.msg.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;

import java.io.Serializable;

/**
 * Created by here on 18/5/10.
 */

public class IMFirstRelation implements Parcelable, Comparable<IMFirstRelation> {
    private String fromUid;
    private String toUid;
    private String fromImUid;
    private String toImUid;
    private int status;
    private long applyTime;
    private long processTime;
    private int relationType;
    private int applyType;
    private GiftInfoObj giftInfoObj;
    private UserInfo userInfo;

    public IMFirstRelation () {

    }

    protected IMFirstRelation(Parcel in) {
        fromUid = in.readString();
        toUid = in.readString();
        fromImUid = in.readString();
        toImUid = in.readString();
        status = in.readInt();
        applyTime = in.readLong();
        processTime = in.readLong();
        relationType = in.readInt();
        applyType = in.readInt();
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
    }

    public static final Creator<IMFirstRelation> CREATOR = new Creator<IMFirstRelation>() {
        @Override
        public IMFirstRelation createFromParcel(Parcel in) {
            return new IMFirstRelation(in);
        }

        @Override
        public IMFirstRelation[] newArray(int size) {
            return new IMFirstRelation[size];
        }
    };

    public boolean checkRecentContact (String imUid) {
        if (fromImUid.equals(imUid) || toImUid.equals(imUid)) {
            return true;
        }
        return false;
    }

    public String getOtherUid () {
        if (userInfo.getUserId().equals(fromUid))
            return toUid;
        return toUid;
    }

    public String getOtherUserImUid () {
        if (userInfo.getUserId().equals(fromUid))
            return fromImUid;
        else
            return toImUid;
//        if (HereUser.getInstance().getImUserInfo() != null) {
//            if (fromImUid.equals(HereUser.getInstance().getImUserInfo().getImUserId())) {
//                return fromImUid;
//            }
//        }
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getFromImUid() {
        return fromImUid;
    }

    public void setFromImUid(String fromImUid) {
        this.fromImUid = fromImUid;
    }

    public String getToImUid() {
        return toImUid;
    }

    public void setToImUid(String toImUid) {
        this.toImUid = toImUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GiftInfoObj getGiftInfoObj() {
        return giftInfoObj;
    }

    public void setGiftInfoObj(GiftInfoObj giftInfoObj) {
        this.giftInfoObj = giftInfoObj;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }

    @Override
    public String toString() {
        return "IMFirstRelation{" +
                "fromUid='" + fromUid + '\'' +
                ", toUid='" + toUid + '\'' +
                ", fromImUid='" + fromImUid + '\'' +
                ", toImUid='" + toImUid + '\'' +
                ", status=" + status +
                ", applyTime=" + applyTime +
                ", processTime=" + processTime +
                ", relationType=" + relationType +
                ", applyType=" + applyType +
                ", giftInfoObj=" + giftInfoObj +
                ", userInfo=" + userInfo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fromUid);
        parcel.writeString(toUid);
        parcel.writeString(fromImUid);
        parcel.writeString(toImUid);
        parcel.writeInt(status);
        parcel.writeLong(applyTime);
        parcel.writeLong(processTime);
        parcel.writeInt(relationType);
        parcel.writeInt(applyType);
        parcel.writeParcelable(userInfo, i);
    }

    @Override
    public int compareTo(@NonNull IMFirstRelation imFirstRelation) {
        if (applyTime < imFirstRelation.getApplyTime()) {
            return 1;
        } else if (applyTime > imFirstRelation.getApplyTime()) {
            return -1;
        }
        return 0;
    }
}
