package com.oneone.api;

import com.oneone.modules.following.dto.FollowListDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

/**
 * Created by here on 18/4/25.
 */

public interface FollowingSub extends ServiceStub {
    @HttpPost("/following/follow")
    ApiResult<Integer> follow(@BodyParameter("userId") String userId);
    @HttpPost("/following/unfollow")
    ApiResult<Integer> unFollow(@BodyParameter("userId") String userId);
    @HttpGet("/following/followers")
    ApiResult<FollowListDto> getFollowers(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);
    @HttpGet("/following/followings")
    ApiResult<FollowListDto> getFollowings(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);
}
