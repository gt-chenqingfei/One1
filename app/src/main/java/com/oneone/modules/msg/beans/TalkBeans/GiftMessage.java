package com.oneone.modules.msg.beans.TalkBeans;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.oneone.modules.msg.beans.GiftProd;

/**
 * Created by here on 18/5/12.
 */

public class GiftMessage extends MyMessage {
    private GiftProd giftProd;
    private IMMessage imMessage;

    public GiftMessage(String text, int type) {
        super(text, type);
    }

    public void init(GiftProd giftProd) {
        this.giftProd = giftProd;
    }

    public GiftProd getGiftProd () {
        return giftProd;
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
