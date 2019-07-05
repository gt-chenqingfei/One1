package com.oneone.modules.msg.event;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by here on 18/5/16.
 */

public class IMMessageEvent {
    List<IMMessage> messageList;

    public IMMessageEvent (List<IMMessage> messageList) {
        this.messageList = messageList;
    }

    public List<IMMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<IMMessage> messageList) {
        this.messageList = messageList;
    }
}
