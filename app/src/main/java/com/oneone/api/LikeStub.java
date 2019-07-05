package com.oneone.api;

import com.oneone.modules.find.dto.LikeListDto;
import com.oneone.modules.find.dto.LikeUnreadListDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.QueryParameter;

/**
 * Created by here on 18/4/24.
 */

public interface LikeStub extends ServiceStub {
    @HttpGet("/like/setlike")
    ApiResult likeSetLike(@QueryParameter("targetUserId") String targetUserId);
    @HttpGet("/like/cancellike")
    ApiResult likeCancelLike(@QueryParameter("targetUserId") String targetUserId);
    @HttpGet("/like/setnofeel")
    ApiResult likeSetNoFeel(@QueryParameter("targetUserId") String targetUserId);
    @HttpGet("/like/cancelnofeel")
    ApiResult likeCancelNoFeel(@QueryParameter("targetUserId") String targetUserId);
    @HttpGet("/like/fromme")
    ApiResult<LikeListDto> likeFromme(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount, @QueryParameter("preReadTime") long preReadTime);
    @HttpGet("/like/tome")
    ApiResult<LikeListDto> likeTome(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount, @QueryParameter("preReadTime") long preReadTime);
    @HttpGet("/like/eachother")
    ApiResult<LikeListDto> likeEachother(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount, @QueryParameter("preReadTime") long preReadTime);
    @HttpGet("/like/unread")
    ApiResult<LikeUnreadListDto> likeUnread();
}
