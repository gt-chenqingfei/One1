package com.oneone.modules.msg.beans;

import java.io.Serializable;

/**
 * Created by here on 18/5/10.
 */

public class GiftProd implements Serializable {
    private String prodCode;
    private String prodName;
    private int amount;
    private String image;
    private String message;

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GiftProd{" +
                "prodCode='" + prodCode + '\'' +
                ", prodName='" + prodName + '\'' +
                ", amount=" + amount +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
