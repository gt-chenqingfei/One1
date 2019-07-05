package com.oneone.modules.entry.presenter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.oneone.BasePresenter;
import com.oneone.Constants;
import com.oneone.OneOne;
import com.oneone.api.constants.ApiStatus;
import com.oneone.framework.android.preference.DefaultSP;
import com.oneone.modules.entry.contract.OpenRelationContract;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;

import java.util.List;


/**
 * @author qingfei.chen
 * @since 2018/3/31.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class OpenRelationPresenter extends BasePresenter<OpenRelationContract.View> implements
        OpenRelationContract.Presenter {
    UserProfileUpdateBean mUserProfileUpdateBean;

    @Override
    public void onAttachView(OpenRelationContract.View view) {
        super.onAttachView(view);
        getTempUserInfo();
    }

    @Override
    public void updateUserByRole(final int role, final UserProfileUpdateBean userInfo,
                                 final OpenRelationContract.OnUserInfoUpdateListener listener) {
        if (userInfo == null) {
            return;
        }

        if (mUserProfileUpdateBean == null) {
            mUserProfileUpdateBean = getTempUserInfo();
        }
        getView().loading("");

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object[] objects) {
                UserModel userModel = new UserModel(getView().getActivityContext());
                ApiResult result = userModel.updateUserInfoByRole(role, new Gson().toJson(userInfo));
                if (result == null) {
                    return null;
                }
                if (result.getStatus() == ApiStatus.OK) {
                    mUserProfileUpdateBean.copy(userInfo);
                    saveTempUserInfo(mUserProfileUpdateBean);
                }
                return result;
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result == null) {
                    return;
                }

                if (result.getStatus() == ApiStatus.OK) {
                    if (listener != null) {
                        listener.onUserInfoUpdate();
                    }
                } else {
                    getView().showError(result.getMessage());
                }

            }
        };

        enqueue(task);

    }


    @Override
    public void openRole(final int role, final OpenRelationContract.OnOpenRoleListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {


            @Override
            protected ApiResult doInBackground(Object[] objects) {
                return new UserModel(getView().getActivityContext()).openRole(role);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result.getStatus() == ApiStatus.OK) {
                    removeTempUserInfo();
                    if (listener != null) {
                        listener.onOpenRole(result.getStatus() == ApiStatus.OK);
                    }
                } else {
                    getView().showError(result.getMessage());
                }

            }
        };
        enqueue(task);
    }

    @Override
    public void addRole(final int role, final OpenRelationContract.OnAddRoleListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().loading("");
            }

            @Override
            protected ApiResult doInBackground(Object[] objects) {
                return new UserModel(getView().getActivityContext()).addRole(role);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                getView().loadingDismiss();
                if (result.getStatus() == ApiStatus.OK) {
                    if (listener != null) {
                        listener.onAddRole(result.getStatus() == ApiStatus.OK);
                    }
                } else {
                    getView().showError(result.getMessage());
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void getUserShowcaseList(final int role, final OpenRelationContract.OnUserShowcaseListListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<List<ShowCaseUserInfo>>>() {

            @Override
            protected ApiResult<List<ShowCaseUserInfo>> doInBackground(Object... voids) {
                return new UserModel(getView().getActivityContext()).getUserShowcaseList(role);
            }

            @Override
            protected void onPostExecute(ApiResult<List<ShowCaseUserInfo>> result) {
                super.onPostExecute(result);

                if (result == null) {
                    return;
                }

                if (listener != null) {
                    listener.onGetUserShowcaseList(role, result.getData());
                }
            }
        };
        enqueue(task);
    }

    @Override
    public void chooseRole(final int role, final OpenRelationContract.OnRoleChooseListener listener) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object... voids) {
                UserModel manager = new UserModel(getView().getActivityContext());

                return manager.chooseRole(role);
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);
                if (listener != null) {
                    listener.onRoleChoose(result.getStatus() == ApiStatus.OK, role);
                }
            }
        };
        enqueue(task);
    }

    public static UserProfileUpdateBean getTempUserInfo() {

        String json = DefaultSP.getInstance().getString(OneOne.getInstance(),
                Constants.PREF.PREF_UPDATE_USER_INFO_BY_ROLE, "");

        UserProfileUpdateBean mUserProfileUpdateBean;
        if (!TextUtils.isEmpty(json)) {
            mUserProfileUpdateBean = new Gson().fromJson(json, UserProfileUpdateBean.class);
        } else {
            mUserProfileUpdateBean = new UserProfileUpdateBean();
        }
        return mUserProfileUpdateBean;
    }

    private void saveTempUserInfo(UserProfileUpdateBean userInfo) {
        if (userInfo == null) {
            return;
        }
        String json = new Gson().toJson(userInfo);
        DefaultSP.getInstance().put(getView().getActivityContext(),
                Constants.PREF.PREF_UPDATE_USER_INFO_BY_ROLE, json).apply();
    }

    private void removeTempUserInfo() {
        DefaultSP.getInstance().remove(getView().getActivityContext(),
                Constants.PREF.PREF_UPDATE_USER_INFO_BY_ROLE).commit();
    }
}
