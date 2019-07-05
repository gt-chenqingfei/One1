package com.oneone.modules.msg.beans.TalkBeans.attachment;

import com.google.gson.Gson;
import com.oneone.modules.msg.beans.GiftProd;
import com.oneone.modules.msg.beans.TalkBeans.TalkGiftProd;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by here on 18/5/9.
 */

public class GiftAttachment extends CustomAttachment {
    private GiftProd gift;

    public GiftAttachment(String type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {
        gift = new Gson().fromJson(data.toString(), GiftProd.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(new Gson().toJson(gift));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    public GiftProd getGift () {
        return gift;
    }

    public void setGift(GiftProd gift) {
        this.gift = gift;
    }
}
