package com.oneone.modules.msg.beans.TalkBeans;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.oneone.modules.msg.beans.IMEmoji;

/**
 * Created by here on 18/5/12.
 */

public class EmojiMessage extends MyMessage {
    private IMEmoji imEmoji;
    private IMMessage imMessage;

    public EmojiMessage(String text, int type) {
        super(text, type);
    }

    public void init (IMEmoji imEmoji) {
        this.imEmoji = imEmoji;
    }

    public IMEmoji getImEmoji () {
        return imEmoji;
    }

    @Override
    public IMMessage getImMessage() {
        return imMessage;
    }

    @Override
    public void setImMessage(IMMessage imMessage) {
        this.imMessage = imMessage;
    }
}
