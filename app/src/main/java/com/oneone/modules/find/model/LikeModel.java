package com.oneone.modules.find.model;

import android.content.Context;

import com.oneone.api.LikeStub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.restful.ApiResult;

/**
 * Created by here on 18/4/24.
 */

public class LikeModel extends BaseModel {
    public static long LAST_TIME_GET_MY_LIKE;
    public static long LAST_TIME_GET_LIKE_ME;
    public static long LAST_TIME_GET_LIKE_EACHOTHER;

    private LikeStub likeStub;

    public LikeModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.likeStub = factory.create(LikeStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult likeSetLike(String userId) {
        ApiResult result = null;
        try {
            result = this.likeStub.likeSetLike(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult likeCancelLike(String userId) {
        ApiResult result = null;
        try {
            result = this.likeStub.likeCancelLike(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult likeSetNoFeel(String userId) {
        ApiResult result = null;
        try {
            result = this.likeStub.likeSetNoFeel(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult likeCancelNoFeel(String userId) {
        ApiResult result = null;
        try {
            result = this.likeStub.likeCancelNoFeel(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<LikeListDto> likeFromme (int skip, int pageCount, long preReadTime) {
        ApiResult<LikeListDto> result = null;
        try {
            result = this.likeStub.likeFromme(skip, pageCount, preReadTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ApiResult<LikeListDto> likeTome (int skip, int pageCount, long preReadTime) {
        ApiResult<LikeListDto> result = null;
        try {
            result = this.likeStub.likeTome(skip, pageCount, preReadTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ApiResult<LikeListDto> likeEachother (int skip, int pageCount, long preReadTime) {
        ApiResult<LikeListDto> result = null;
        try {
            result = this.likeStub.likeEachother(skip, pageCount, preReadTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ApiResult<LikeUnreadListDto> likeUnread () {
        ApiResult<LikeUnreadListDto> result = null;
        try {
            result = this.likeStub.likeUnread();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
