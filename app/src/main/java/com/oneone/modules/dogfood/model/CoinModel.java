package com.oneone.modules.dogfood.model;

import android.content.Context;

import com.google.gson.JsonObject;
import com.oneone.api.CoinSub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.dogfood.beans.Balance;
import com.oneone.modules.dogfood.dto.CoinRecordDto;
import com.oneone.restful.ApiResult;

import org.json.JSONObject;

/**
 * Created by here on 18/5/2.
 */

public class CoinModel extends BaseModel {
    private CoinSub coinSub;

    public CoinModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.coinSub = factory.create(CoinSub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<JSONObject> coinGetcoinbalance() {
        ApiResult<JSONObject> result = null;
        try {
            result = this.coinSub.coinGetcoinbalance();
            System.out.println("==============>" + result);
            System.out.println("==============>" + result.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<CoinRecordDto> coinRecords(int skip, int pageCount) {
        ApiResult<CoinRecordDto> result = null;
        try {
            result = this.coinSub.coinRecords(skip, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
