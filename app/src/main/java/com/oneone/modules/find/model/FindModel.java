package com.oneone.modules.find.model;

import android.content.Context;

import com.oneone.api.FindSub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.find.beans.FindCondition;
import com.oneone.modules.find.dto.FindPageUserInfoListDto;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public class FindModel extends BaseModel {
    private FindSub findSub;

    public FindModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.findSub = factory.create(FindSub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<FindPageUserInfoListDto> findRecommend() {
        ApiResult<FindPageUserInfoListDto> result = null;
        try {
            result = this.findSub.findRecommend();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult findSetCondition(String findConditionStr) {
        ApiResult result = null;
        try {
            result = this.findSub.findSetCondition(findConditionStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<FindCondition> findGetCondition() {
        ApiResult<FindCondition> result = null;
        try {
            result = this.findSub.findGetCondition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
