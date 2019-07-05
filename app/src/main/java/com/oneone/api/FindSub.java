package com.oneone.api;

import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoListDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyJsonParameter;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public interface FindSub extends ServiceStub {
    @HttpGet("/find/recommend")
    ApiResult<FindPageUserInfoListDto> findRecommend();
    @HttpPost("/find/setcondition")
    ApiResult findSetCondition(@BodyJsonParameter("character")String findConditionStr);
    @HttpGet("/find/getcondition")
    ApiResult<FindCondition> findGetCondition();
}
