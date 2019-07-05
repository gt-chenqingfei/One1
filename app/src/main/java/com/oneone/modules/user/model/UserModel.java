package com.oneone.modules.user.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.main.me.contract.MineContract;
import com.oneone.modules.user.bean.ShowCaseUserInfo;
import com.oneone.modules.user.bean.UserStatisticInfo;
import com.oneone.modules.user.dto.UserInfoDTO;
import com.oneone.modules.user.bean.UserInfo;
import com.oneone.api.UserStub;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/4.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class UserModel extends BaseModel {

    private UserStub userStub = null;

    public UserModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.userStub = factory.create(UserStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<UserInfoDTO> getUserInfo(String userId) {
        ApiResult<UserInfoDTO> result = null;
        try {
            result = this.userStub.getUserInfo(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<UserInfo> getUserRoleInfo(int role) {
        ApiResult<UserInfo> result = null;
        try {
            result = this.userStub.getUserRoleInfo(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult updateUserInfoByRole(int role, String myStoryImgJsonStr) {
        ApiResult result = null;
        try {
            result = this.userStub.updateUserInfoByRole(role, myStoryImgJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult updateUserInfo(String userSettingInfo) {
        ApiResult result = null;
        try {
            result = this.userStub.updateUserInfo(userSettingInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult chooseRole(int role) {
        ApiResult result = null;
        try {
            result = this.userStub.chooseRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult openRole(int role) {
        ApiResult result = null;
        try {
            result = this.userStub.openRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult addRole(int role) {
        ApiResult result = null;
        try {
            result = this.userStub.addRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<List<ShowCaseUserInfo>> getUserShowcaseList(int role) {
        ApiResult<List<ShowCaseUserInfo>> result = null;
        try {
            result = this.userStub.getUserShowcaseList(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<UserStatisticInfo> getStatisticUserQueryInfo() {
        ApiResult<UserStatisticInfo> result = null;

        try {
            result = this.userStub.getStatisticUserQueryInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
