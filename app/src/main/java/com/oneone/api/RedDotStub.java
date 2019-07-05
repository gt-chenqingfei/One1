package com.oneone.api;

import com.oneone.modules.reddot.dto.RedDotDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by here on 18/4/24.
 */

public interface RedDotStub extends ServiceStub {
    @HttpGet("/reddot/query")
    ApiResult<Object> query(@QueryParameter("types") String types);
    @HttpPost("/reddot/clear")
    ApiResult clear(@BodyJsonParameter("types") String types);
}
