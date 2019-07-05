package com.oneone.modules.msg.beans;

import java.io.Serializable;

/**
 * Created by here on 18/5/10.
 */

public class IMEmoji implements Serializable {
    private String code;
    private String image;
    private String message;
    private String type = "emoji";

    private int sort;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "IMEmoji{" +
                "code='" + code + '\'' +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", sort=" + sort +
                '}';
    }
}
