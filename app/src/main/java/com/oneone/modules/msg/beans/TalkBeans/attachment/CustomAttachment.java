package com.oneone.modules.msg.beans.TalkBeans.attachment;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONObject;

public abstract class CustomAttachment implements MsgAttachment {

    // 自定义消息附件的类型，根据该字段区分不同的自定义消息
    protected String type;

    protected CustomAttachment(String type) {
        this.type = type;
    }

    // 解析附件内容。
    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    // 实现 MsgAttachment 的接口，封装公用字段，然后调用子类的封装函数。
    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(type, packData());
    }

    // 子类的解析和封装接口。
    protected abstract void parseData(JSONObject data);
    protected abstract JSONObject packData();
}