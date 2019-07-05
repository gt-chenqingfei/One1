package com.oneone.api;


import com.oneone.modules.support.bean.City;
import com.oneone.modules.support.bean.Occupation;
import com.oneone.modules.entry.beans.UploadTokenBean;
import com.oneone.modules.support.bean.ShareInfo;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;

import java.util.List;

/**
 * create by qingfei.chen
 */
public interface SupportStub extends ServiceStub {

    @HttpGet("/support/uploadtoken")
    ApiResult<UploadTokenBean> getUploadToken();

    @HttpGet("/support/occupationlist")
    ApiResult<List<Occupation>> occupationList();

    @HttpGet("/support/citylist")
    ApiResult<List<City>> citylist();

    @HttpGet("/support/sharesettings")
    ApiResult sharedInfo();

    @HttpPost("/support/deviceinfo")
    ApiResult deviceInfo(@BodyParameter("deviceToken") String deviceToken,
                         @BodyParameter("channel") String channel);
}
