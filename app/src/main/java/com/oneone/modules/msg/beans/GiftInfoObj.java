package com.oneone.modules.msg.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by here on 18/5/10.
 */

public class GiftInfoObj implements Parcelable {
    private String prodCode;
    private String image;
    private int amount;
    private String prodName;
    private String message;

    protected GiftInfoObj(Parcel in) {
        prodCode = in.readString();
        image = in.readString();
        amount = in.readInt();
        prodName = in.readString();
        message = in.readString();
    }

    public static final Creator<GiftInfoObj> CREATOR = new Creator<GiftInfoObj>() {
        @Override
        public GiftInfoObj createFromParcel(Parcel in) {
            return new GiftInfoObj(in);
        }

        @Override
        public GiftInfoObj[] newArray(int size) {
            return new GiftInfoObj[size];
        }
    };

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(prodCode);
        parcel.writeString(image);
        parcel.writeInt(amount);
        parcel.writeString(prodName);
        parcel.writeString(message);
    }
}
