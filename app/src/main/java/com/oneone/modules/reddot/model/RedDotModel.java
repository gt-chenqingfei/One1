package com.oneone.modules.reddot.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oneone.api.RedDotStub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.constants.ApiStatus;
import com.oneone.api.constants.RedDot;
import com.oneone.api.request.RedDotTypesParams;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.reddot.dto.RedDotDto;
import com.oneone.restful.ApiResult;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author qingfei.chen
 * @since 2018/7/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class RedDotModel extends BaseModel {
    private RedDotStub mStub = null;

    public RedDotModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.mStub = factory.create(RedDotStub.class,
                RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public Object queryRedDot(String types) {
        ApiResult<Object> result = mStub.query(types);
        if (result != null && result.getStatus() == ApiStatus.OK) {
            return result.getData();
        }
        return null;
    }

    public boolean clearRedDot(RedDotTypesParams types) {
        ApiResult result = mStub.clear(new Gson().toJson(types));
        if (result != null && result.getStatus() == ApiStatus.OK) {
            return true;
        }
        return false;
    }
}
