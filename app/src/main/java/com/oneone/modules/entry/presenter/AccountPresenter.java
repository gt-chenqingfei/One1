package com.oneone.modules.entry.presenter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.ApiStatus;
import com.oneone.api.constants.Role;
import com.oneone.framework.android.utils.LocaleUtils;
import com.oneone.HereSingletonFactory;
import com.oneone.modules.entry.beans.AccountInfo;
import com.oneone.modules.entry.beans.LoginInfo;
import com.oneone.modules.entry.contract.AccountContract;
import com.oneone.modules.entry.model.AccountModel;
import com.oneone.modules.msg.IMManager;
import com.oneone.modules.msg.beans.IMEmoji;
import com.oneone.modules.msg.beans.IMUser;
import com.oneone.modules.msg.dto.GiftProdDto;
import com.oneone.modules.qa.QaDataManager;
import com.oneone.modules.qa.beans.QuestionClassify;
import com.oneone.modules.qa.dto.QaCountForallDto;
import com.oneone.modules.qa.dto.QuestionListDto;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.UserManager;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;
import com.oneone.utils.ToastUtil;

import java.util.List;


/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class AccountPresenter extends BasePresenter<AccountContract.View> implements
        AccountContract.Presenter {
    private AccountModel mAccountModel;
    private UserModel userModel;

    @Override
    public void onAttachView(AccountContract.View view) {
        super.onAttachView(view);
        mAccountModel = new AccountModel(getView().getActivityContext());
        userModel = new UserModel(getView().getActivityContext());
    }

    @Override
    public void loginByThirdPart(final String platform, final String platformId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<LoginInfo>>() {

            @Override
            protected ApiResult<LoginInfo> doInBackground(Object[] objects) {
                ApiResult<LoginInfo> result = mAccountModel
                        .loginWithThirdPlatform(platform, platformId);

                if (result.getData() == null) {
                    return result;
                }
                HereUser.newHereUser(result.getData());
                return result;
            }

            @Override
            protected void onPostExecute(ApiResult<LoginInfo> result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result == null) {
                    return;
                }

                if (result.getStatus() == ApiStatus.NO_BIND_PHONE) {
                    getView().goBindPhone(platform, platformId);
                    return;
                }
                if (result.getStatus() != ApiStatus.OK) {
                    if (result.getMessage() == null) {
                        return;
                    } else {
                        getView().showError(result.getMessage());
                        return;
                    }
                }
                if (result.getData().getAccountRole() != Role.UNKNOWN) {
                    getView().goHome();
                    return;
                }
                getView().goUserRoleSelect();
            }
        };

        enqueue(task);
    }

    @Override
    public void loginByPhone(final String phoneNumber, final String validCode) {
        final String country = LocaleUtils.getCountryCodeByPhoneNum(phoneNumber);
        HereSingletonFactory.getInstance().getUserManager().setCountryCode(country);
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<LoginInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult<LoginInfo> doInBackground(Object[] objects) {
                ApiResult<LoginInfo> result = mAccountModel.loginByMobile(country, phoneNumber, validCode);
                if (result.getData() == null) {
                    return result;
                }
                HereUser.newHereUser(result.getData());
                return result;
            }

            @Override
            protected void onPostExecute(ApiResult<LoginInfo> result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                getView().validCodeEditViewEnable();
                if (result == null) {
                    return;
                }

                if (result.getStatus() != ApiStatus.OK) {
                    getView().showError(result.getMessage());
                    return;
                }

                LoginInfo loginInfo = result.getData();
                if (loginInfo.getAccountRole() != Role.UNKNOWN) {
                    //Has register and has open role
                    getView().goHome();
                    return;
                }

                if (!loginInfo.isAlreadyBindWechat()) {
                    // Not register need bind WeChat if needed
                    getView().goBindWXIfInstall();

                } else {
                    // Not register but already bind WeChat
                    getView().goUserRoleSelect();
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void bindMobile(final String mobileCountryCode, final String mobilePhoneNum,
                           final String validateCode, final String platform, final String platformId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<LoginInfo>>() {

            @Override
            protected ApiResult<LoginInfo> doInBackground(Object... voids) {
                getView().loadingDismiss();
                ApiResult result = mAccountModel.bindMobile(
                        mobileCountryCode, mobilePhoneNum,
                        validateCode, platform, platformId);

                return result;
            }

            @Override
            protected void onPostExecute(ApiResult<LoginInfo> result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result == null) {
                    return;
                }

                switch (result.getStatus()) {
                    case ApiStatus.VALID_CODE_ERROR:
                        //verifyCodeEt.requestFocus();
                        showError(result.getMessage());
                        return;

                    case ApiStatus.MOBILE_HAS_BIND_OTHER_ACCOUNT:
                    case ApiStatus.THIRD_ACCOUNT_HAS_BIND_PHONE:
                        showError(result.getMessage());
                        return;
                }
                loginByPhone(mobilePhoneNum, validateCode);
            }
        };
        enqueue(task);
    }

    @Override
    public void getValidateCode(final String phoneNum) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult doInBackground(Object[] objects) {
                String country = LocaleUtils.getCountryCodeByPhoneNum(phoneNum);
                ApiResult result = mAccountModel.loginValidateCode(country, phoneNum);

                return result;
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
//                ToastUtil.show(getView().getActivityContext(), result.getMessage());
                getView().onValidCodeGet(result.getStatus());
            }
        };
        enqueue(task);
    }


    @Override
    public void bindThird(final String platform, final String platformId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                return mAccountModel.bindThird(HereUser.getInstance().getLoginInfo().getUserId(), platform, platformId);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();

                if (result == null) {
                    return;
                }

                switch (result.getStatus()) {
                    case ApiStatus.USER_NOT_EXIST:
                    case ApiStatus.COUNTRY_CODE_ERROR:
                    case ApiStatus.VALID_CODE_ERROR:
                        getView().showError(result.getMessage());
                        return;
                    case ApiStatus.THIRD_ACCOUNT_HAS_BIND_PHONE:
                        getView().alreadyBindWX(platform, platformId);
                        return;
                }
                getView().goUserRoleSelect();
            }
        };
        enqueue(task);
    }


}
