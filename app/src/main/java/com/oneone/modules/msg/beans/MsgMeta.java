package com.oneone.modules.msg.beans;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by here on 18/5/15.
 */

public class MsgMeta implements Comparable {
    private String userId;
    private Long timestamp = new Long(0);
    private String bizType;
    private String metaTitle;
    private int metaType;
    private int msgType;
    private String metaValue = "";
    private String picUrl;
    private String linkTitle;
    private String linkUrl;
    private Map ext;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public int getMetaType() {
        return metaType;
    }

    public void setMetaType(int metaType) {
        this.metaType = metaType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Map getExt() {
        return ext;
    }

    public void setExt(Map ext) {
        this.ext = ext;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        MsgMeta otherObj = (MsgMeta) o;
        if (this.timestamp > ((MsgMeta) o).timestamp)
            return 1;
        else if (this.timestamp < ((MsgMeta) o).timestamp)
            return -1;
        return 0;
    }
}
