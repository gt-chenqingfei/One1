package com.oneone.modules.profile.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.UserStub;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.profile.bean.IntersectionInfo;
import com.oneone.restful.ApiResult;

public class IntersectionModel extends BaseModel {

    private UserStub userStub = null;

    public IntersectionModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.userStub = factory.create(UserStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<IntersectionInfo> getUserntersectionInfo(String targetUserId) {
        ApiResult<IntersectionInfo> result = null;
        try {
            result = this.userStub.getUserntersectionInfo(targetUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
