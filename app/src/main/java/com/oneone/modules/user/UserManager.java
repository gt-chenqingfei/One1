package com.oneone.modules.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.oneone.HereSingletonFactory;
import com.oneone.OneOne;
import com.oneone.api.constants.ApiStatus;
import com.oneone.api.constants.Role;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.modules.user.bean.UserProfileUpdateBean;
import com.oneone.modules.user.dto.UserInfoDTO;
import com.oneone.modules.user.model.UserModel;
import com.oneone.restful.ApiResult;

public class UserManager {

    public interface UserUpdateListener {
        void onUserUpdate(UserInfo userInfo, boolean isOk, String message);
    }

    public interface UserUpdateByRoleListener {
        void onUserUpdateByRole(UserInfo userInfo, boolean isOk, String message);
    }

    private Context mContext;
    private String countryCode;

    public UserManager(Context context) {
        this.mContext = context;
    }


    public void updateUserInfo(final UserUpdateListener listener, final UserProfileUpdateBean bean) {
        if (bean == null) {
            listener.onUserUpdate(HereUser.getInstance().getUserInfo(), false, "");
            return;
        }
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult>() {

            @Override
            protected ApiResult doInBackground(Object[] objects) {
                UserModel userModel = new UserModel(mContext);
                ApiResult apiResult = userModel.updateUserInfo(new Gson().toJson(bean));
                fetchUserInfoSync();
                return apiResult;
            }

            @Override
            protected void onPostExecute(ApiResult result) {
                super.onPostExecute(result);

                if (result == null) {
                    return;
                }

                if (listener != null) {
                    listener.onUserUpdate(HereUser.getInstance().getUserInfo(),
                            result.getStatus() == ApiStatus.OK, result.getMessage());
                }

            }
        };
        task.execute();
    }

    public void fetchUserInfo() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Object, Void, ApiResult<UserInfoDTO>>() {

            @Override
            protected ApiResult<UserInfoDTO> doInBackground(Object[] objects) {
                return fetchUserInfoSync();
            }
        };
        task.execute();
    }

    public ApiResult<UserInfoDTO> fetchUserInfoSync() {
        HereUser user = HereUser.getInstance();
        UserModel userModel = new UserModel(mContext);
        ApiResult<UserInfoDTO> result1 =
                userModel.getUserInfo(user.getLoginInfo().getUserId());
        if (result1.getStatus() == ApiStatus.OK) {
            UserInfoDTO dto = result1.getData();
            UserInfo info = dto.getUserInfo();
            info.setFollow(dto.getFollow());
            if (dto.getRelationInfo() != null) {
                info.setLikeStatus(dto.getRelationInfo().getLikeStatus());
                info.setNoFeelStatus(dto.getRelationInfo().getNoFeelStatus());
            }
            info.setIntersectionValue(dto.getIntersectionValue());
            info.setMatchValue(dto.getMatchValue());
            info.setQaAnswer(dto.getQaAnswer());
            user.updateUserInfo(result1.getData().getUserInfo());
        }
        return result1;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
