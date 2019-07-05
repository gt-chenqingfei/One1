package com.oneone.modules.entry.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.entry.beans.AccountInfo;
import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.api.AccountStub;
import com.oneone.restful.ApiResult;

/**
 * Created by chenqingfei on 16/7/18.
 */
public class AccountModel extends BaseModel {
    private AccountStub accountStub = null;

    public AccountModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.accountStub = factory.create(AccountStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public AccountInfo accountUserInfo() {
        AccountInfo accountInfo = null;
        try {
            ApiResult<AccountInfo> result = this.accountStub.accountUserInfo();
            if (null == result) {
                return null;
            }
            accountInfo = result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountInfo;
    }

    public LoginInfo uploadThirdPlatformUserInfo(String platform, String platformId, String openid
            , String nickname, int sex, String headimgurl, String language, String country, String province, String city) {
        LoginInfo responseLoginUserInfo = null;
        try {
            ApiResult<LoginInfo> result = this.accountStub.uploadThirdPlatformUserInfo(platform, platformId, openid
                    , nickname, sex, headimgurl, language, country, province, city);
            if (null == result) {
                return null;
            }
            responseLoginUserInfo = result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseLoginUserInfo;
    }

    public ApiResult bindThird(String userId, String platform, String platformId) {
        ApiResult result = null;
        try {
            result = this.accountStub.bindThird(userId, platform, platformId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult bindMobile(String mobileCountryCode, String mobilePhoneNum, String validateCode, String platform, String platformId) {
        ApiResult result = null;
        try {
            result = this.accountStub.bindMobile(mobileCountryCode, mobilePhoneNum, validateCode, platform, platformId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<LoginInfo> loginWithThirdPlatform(String platform, String platformId) {
        ApiResult<LoginInfo> result = null;
        try {
            result = this.accountStub.loginWithThirdPlatform(platform, platformId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult loginValidateCode(String mobileCountryCode, String mobilePhoneNum) {
        ApiResult result = null;
        try {
            result = this.accountStub.loginValidateCode(mobileCountryCode, mobilePhoneNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<LoginInfo> loginByMobile(String mobileCountryCode, String mobilePhoneNum, String validateCode) {
        try {
            return this.accountStub.loginByMobile(mobileCountryCode, mobilePhoneNum, validateCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
