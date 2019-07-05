package com.oneone.modules.msg.beans.TalkBeans;

import android.support.annotation.NonNull;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.oneone.modules.msg.beans.IMFirstRelation;
import com.oneone.modules.msg.dto.MsgMetaDto;

import java.io.Serializable;

/**
 * Created by here on 18/5/21.
 */

public class MyRecentContact implements Comparable<MyRecentContact> {
    private MsgMetaDto msgMetaDto;
    private RecentContact recentContact;
    private IMFirstRelation firstRelation;

    private boolean isDelete = false;

    public void setMyDelete () {
        isDelete = true;
    }

    public boolean isDeleted () {
        return isDelete;
    }

    public RecentContact getRecentContact() {
        return recentContact;
    }

    public void setRecentContact(RecentContact recentContact) {
        this.recentContact = recentContact;
    }

    public IMFirstRelation getFirstRelation() {
        return firstRelation;
    }

    public void setFirstRelation(IMFirstRelation firstRelation) {
        this.firstRelation = firstRelation;
    }

    public MsgMetaDto getMsgMetaDto() {
        return msgMetaDto;
    }

    public void setMsgMetaDto(MsgMetaDto msgMetaDto) {
        this.msgMetaDto = msgMetaDto;
    }

    public String getMyTargetId () {
        if (recentContact != null)
            return recentContact.getContactId();
        else if (firstRelation != null)
            return firstRelation.getOtherUserImUid();
        return "";
    }

    public long getMyTime () {
        if (recentContact != null)
            return recentContact.getTime();
        else if (firstRelation != null)
            return firstRelation.getProcessTime();

        return 0;
    }

    @Override
    public int compareTo(@NonNull MyRecentContact myRecentContact) {
        long myTime;
        if (msgMetaDto != null && msgMetaDto.getMsgMeta() != null) {
            myTime = msgMetaDto.getMsgMeta().getTimestamp();
        } else {
            myTime = getMyTime();
        }
        long otherTime;
        if (myRecentContact.getMsgMetaDto() != null) {
            otherTime = myRecentContact.getMsgMetaDto().getMsgMeta().getTimestamp();
        } else {
            otherTime = myRecentContact.getMyTime();
        }
        if (myTime > otherTime)
            return -1;
        else
            return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MyRecentContact) {
            MyRecentContact otherContact = (MyRecentContact) obj;
            if (msgMetaDto != null && otherContact.msgMetaDto != null) {
                return true;
            } else if (msgMetaDto != null || otherContact.msgMetaDto != null) {
                return false;
            }

            if (getMyTargetId().equals(otherContact.getMyTargetId())) {
                return true;
            } else {
                return false;
            }
        }
        return super.equals(obj);
    }
}
