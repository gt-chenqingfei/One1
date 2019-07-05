package com.oneone.modules.mystory.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class StoryImg implements Serializable, Cloneable {
    private int id;
    private String url;
    private String caption = "";
    private int width;
    private int height;
    private int groupIndex;
    private int orderIndex;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public StoryImg clone() {
        try {
            return (StoryImg) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }
}
