package com.oneone.api;


import com.oneone.modules.entry.beans.AccountInfo;
import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;


/**
 * create by qingfei.chen
 */
public interface AccountStub extends ServiceStub {
    @HttpGet("/account/info")
    ApiResult<AccountInfo> accountUserInfo();

    @HttpPost("/account/thirdplatforminfo")
    ApiResult<LoginInfo> uploadThirdPlatformUserInfo(@BodyParameter("platform") String platform
            , @BodyParameter("platformId") String platformId
            , @BodyParameter("openid") String openid
            , @BodyParameter("nickname") String nickname
            , @BodyParameter("sex") int sex
            , @BodyParameter("headimgurl") String headimgurl
            , @BodyParameter("language") String language
            , @BodyParameter("country") String country
            , @BodyParameter("province") String province
            , @BodyParameter("city") String city);

    @HttpPost("/account/bindthird")
    ApiResult bindThird(@BodyParameter("userId") String userId
            , @BodyParameter("platform") String platform
            , @BodyParameter("platformId") String platformId);

    @HttpPost("/account/bindmobile")
    ApiResult bindMobile(@BodyParameter("mobileCountryCode") String mobileCountryCode
            , @BodyParameter("mobilePhoneNum") String mobilePhoneNum
            , @BodyParameter("validateCode") String validateCode
            , @BodyParameter("platform") String platform
            , @BodyParameter("platformId") String platformId);

    @HttpPost("/account/login/thirdplatform")
    ApiResult<LoginInfo> loginWithThirdPlatform(@BodyParameter("platform") String platform
            , @BodyParameter("platformId") String platformId);

    @HttpPost("/account/login/mobile/validatecode")
    ApiResult loginValidateCode(@BodyParameter("mobileCountryCode") String mobileCountryCode
            , @BodyParameter("mobilePhoneNum") String mobilePhoneNum);

    @HttpPost("/account/login/mobile")
    ApiResult<LoginInfo> loginByMobile(@BodyParameter("mobileCountryCode") String mobileCountryCode
            , @BodyParameter("mobilePhoneNum") String mobilePhoneNum
            , @BodyParameter("validateCode") String validateCode);
}
