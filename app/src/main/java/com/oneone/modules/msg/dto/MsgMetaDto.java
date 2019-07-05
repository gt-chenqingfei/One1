package com.oneone.modules.msg.dto;

import com.oneone.modules.msg.beans.MsgMeta;

/**
 * Created by here on 18/5/15.
 */

public class MsgMetaDto {
    private int unread;
    private MsgMeta msgMeta;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public MsgMeta getMsgMeta() {
        return msgMeta;
    }

    public void setMsgMeta(MsgMeta msgMeta) {
        this.msgMeta = msgMeta;
    }
}
