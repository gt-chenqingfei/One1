package com.oneone.modules.msg.event;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by here on 18/5/23.
 */

public class IMHistoryMessageEvent {
    List<IMMessage> msgList;

    public IMHistoryMessageEvent (List<IMMessage> msgList) {
        this.msgList = msgList;
    }

    public List<IMMessage> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<IMMessage> msgList) {
        this.msgList = msgList;
    }
}
