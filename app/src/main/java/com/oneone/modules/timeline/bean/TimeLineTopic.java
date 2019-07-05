package com.oneone.modules.timeline.bean;

/**
 * @author qingfei.chen
 * @since 2018/6/1.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class TimeLineTopic {
    private int id;
    private String tag;
    private String tagHash;
    private boolean operationFlag;
    private String bgImgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagHash() {
        return tagHash;
    }

    public void setTagHash(String tagHash) {
        this.tagHash = tagHash;
    }

    public boolean isOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(boolean operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl;
    }
}
