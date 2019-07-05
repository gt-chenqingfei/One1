package com.oneone.modules.msg.beans.TalkBeans.attachment;

import com.google.gson.Gson;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.TalkBeans.attachment.CustomAttachment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by here on 18/5/9.
 */

public class EmojiAttachment extends CustomAttachment {
    private IMEmoji imEmoji;

    public EmojiAttachment(String type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {
        imEmoji = new Gson().fromJson(data.toString(), IMEmoji.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(new Gson().toJson(imEmoji));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    public IMEmoji getImEmoji() {
        return imEmoji;
    }

    public void setImEmoji(IMEmoji imEmoji) {
        this.imEmoji = imEmoji;
    }
}
