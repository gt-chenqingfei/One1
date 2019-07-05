package com.oneone.api;

import com.oneone.modules.dogfood.dto.CoinRecordDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.QueryParameter;

import org.json.JSONObject;

/**
 * Created by here on 18/5/2.
 */

public interface CoinSub extends ServiceStub {
    @HttpGet("/coin/getcoinbalance")
    ApiResult<JSONObject> coinGetcoinbalance();

    @HttpGet("/coin/records")
    ApiResult<CoinRecordDto> coinRecords(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);
}
