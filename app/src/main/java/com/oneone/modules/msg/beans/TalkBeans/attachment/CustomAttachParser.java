package com.oneone.modules.msg.beans.TalkBeans.attachment;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomAttachParser implements MsgAttachmentParser {
    public static final String KEY_TYPE = "type";

    public static final String IM_TYPE_GIFT = "gift";
    public static final String IM_TYPE_EMOJI = "emoji";

    // 根据解析到的消息类型，确定附件对象类型
    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = new JSONObject(json);
            String type = object.getString("type");
            JSONObject data = null;
            if (type.equals(IM_TYPE_GIFT)) {
                attachment = new GiftAttachment(IM_TYPE_GIFT);
                data = object.getJSONObject("gift");
            } else if (type.equals(IM_TYPE_EMOJI)) {
                attachment = new EmojiAttachment(IM_TYPE_EMOJI);
                data = object.getJSONObject("emoji");
            }

            if (data != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attachment;
    }

    public static String packData(String type, JSONObject data) {
        JSONObject object = new JSONObject();
        try {
            object.put(KEY_TYPE, type);
            if (data != null) {
                if (type.equals(IM_TYPE_GIFT)) {
                    object.put(IM_TYPE_GIFT, data);
                } else if (type.equals(IM_TYPE_EMOJI)) {
                    object.put(IM_TYPE_EMOJI, data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
}