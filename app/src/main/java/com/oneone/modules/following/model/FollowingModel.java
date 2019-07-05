package com.oneone.modules.following.model;

import android.content.Context;

import com.oneone.api.FollowingSub;
import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.event.EventFollowChanged;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.following.dto.FollowListDto;
import com.oneone.restful.ApiResult;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by here on 18/4/25.
 */

public class FollowingModel extends BaseModel {
    private FollowingSub followingSub;

    public FollowingModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.followingSub = factory.create(FollowingSub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<Integer> follow(String userId) {
        ApiResult<Integer> result = null;
        try {
            result = this.followingSub.follow(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new EventFollowChanged());
        return result;
    }

    public ApiResult<Integer> unFollow(String userId) {
        ApiResult<Integer> result = null;
        try {
            result = this.followingSub.unFollow(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new EventFollowChanged());
        return result;
    }

    public ApiResult<FollowListDto> getFollowers(int skip, int pageCount) {
        ApiResult<FollowListDto> result = null;
        try {
            result = this.followingSub.getFollowers(skip, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult<FollowListDto> getFollowings(int skip, int pageCount) {
        ApiResult<FollowListDto> result = null;
        try {
            result = this.followingSub.getFollowings(skip, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
