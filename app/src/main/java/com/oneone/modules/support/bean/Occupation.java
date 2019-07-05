package com.oneone.modules.support.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Occupation implements Parcelable {
    private String name;
    private String parentCode;
    private String code;
    private int order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.parentCode);
        dest.writeString(this.code);
        dest.writeInt(this.order);
    }

    public Occupation() {
    }

    protected Occupation(Parcel in) {
        this.name = in.readString();
        this.parentCode = in.readString();
        this.code = in.readString();
        this.order = in.readInt();
    }

    public static final Parcelable.Creator<Occupation> CREATOR = new Parcelable.Creator<Occupation>() {
        @Override
        public Occupation createFromParcel(Parcel source) {
            return new Occupation(source);
        }

        @Override
        public Occupation[] newArray(int size) {
            return new Occupation[size];
        }
    };

    @Override
    public String toString() {
        return "Occupation{" +
                "name='" + name + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", code='" + code + '\'' +
                ", order=" + order +
                '}';
    }
}
