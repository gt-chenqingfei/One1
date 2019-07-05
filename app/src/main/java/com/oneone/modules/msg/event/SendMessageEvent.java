package com.oneone.modules.msg.event;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by here on 18/7/13.
 */

public class SendMessageEvent {
    private IMMessage imMessage;
    private boolean rlt;

    public SendMessageEvent (IMMessage imMessage, boolean rlt) {
        this.imMessage = imMessage;
        this.rlt = rlt;
    }

    public IMMessage getImMessage() {
        return imMessage;
    }

    public void setImMessage(IMMessage imMessage) {
        this.imMessage = imMessage;
    }

    public boolean isRlt() {
        return rlt;
    }

    public void setRlt(boolean rlt) {
        this.rlt = rlt;
    }
}
