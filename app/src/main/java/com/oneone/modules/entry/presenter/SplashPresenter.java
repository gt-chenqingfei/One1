package com.oneone.modules.entry.presenter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.api.constants.AccountStatus;
import com.oneone.api.constants.ApiStatus;
import com.oneone.modules.entry.beans.AccountInfo;
import com.oneone.modules.entry.contract.SplashContract;
import com.oneone.modules.entry.model.AccountModel;
import com.oneone.modules.user.HereUser;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.dto.UserInfoDTO;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;


/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class SplashPresenter extends BasePresenter<SplashContract.View> implements
        SplashContract.Presenter {
    private AccountModel mAccountModel;
    private UserModel mUserMode;
    HereUser mHereUser;

    @Override
    public void onAttachView(SplashContract.View view) {
        super.onAttachView(view);
        mAccountModel = new AccountModel(getView().getActivityContext());
        mUserMode = new UserModel(getView().getActivityContext());
        mHereUser = HereUser.getInstance();
    }

    @Override
    public void getLoginInfo(final SplashContract.OnLoginInfoListener loginInfoListener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... voids) {
                AccountInfo accountInfo = mAccountModel.accountUserInfo();
                mHereUser.updateUserInfo(accountInfo);
                if (accountInfo == null
                        || accountInfo.getAccountStatus() != AccountStatus.STATUS_NORMAL) {
                    return null;
                }

                ApiResult<UserInfo> result = mUserMode.getUserRoleInfo(accountInfo.getAccountRole());
                if (result.getStatus() == ApiStatus.OK) {
                    if (result.getData() != null) {
                        mHereUser.updateUserInfo(result.getData());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mHereUser.getLoginInfo() == null ||
                        mHereUser.getLoginInfo().getAccountStatus() != AccountStatus.STATUS_NORMAL) {
                    getView().goAccountException();
                    return;
                }
                if (loginInfoListener != null) {
                    loginInfoListener.onLoginInfo(mHereUser.getLoginInfo());
                }
            }
        };
        enqueue(task);
    }

}
