package com.oneone.modules.dogfood.beans;

import com.oneone.modules.user.bean.UserInfo;

/**
 * Created by here on 18/5/2.
 */

public class CoinRecord {
    private String recordId;
    private String eventId;
    private String userId;
    private int amount;
    private int coinType;
    private String coinTypeDesc;
    private String refRecordId;
    private String targetId;
    private UserInfo targetInfo;
    private Long gmtCreate;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public String getCoinTypeDesc() {
        return coinTypeDesc;
    }

    public void setCoinTypeDesc(String coinTypeDesc) {
        this.coinTypeDesc = coinTypeDesc;
    }

    public String getRefRecordId() {
        return refRecordId;
    }

    public void setRefRecordId(String refRecordId) {
        this.refRecordId = refRecordId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public UserInfo getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(UserInfo targetInfo) {
        this.targetInfo = targetInfo;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
