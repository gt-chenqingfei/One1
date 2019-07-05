package com.oneone.api;


import com.oneone.modules.matcher.relations.dto.MatcherCountDto;
import com.oneone.modules.matcher.relations.dto.MyMatchersDto;
import com.oneone.modules.matcher.relations.dto.MySinglesDto;
import com.oneone.modules.matcher.relations.dto.SingleCountDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.BodyParameter;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.HttpPost;
import com.oneone.restful.annotation.QueryParameter;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/18.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public interface MatcherRelationsStub extends ServiceStub {
    @HttpGet("/matcherrelation/mysingles")
    ApiResult<MySinglesDto> getMySingles(@QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);

    @HttpGet("/matcherrelation/mymatchers")
    ApiResult<MyMatchersDto> getMyMatcher();

    @HttpGet("/matcherrelation/otherssingles")
    ApiResult<MySinglesDto> getOthersSingles(@QueryParameter("userId") String userId,
                                             @QueryParameter("skip") int skip, @QueryParameter("pageCount") int pageCount);

    @HttpGet("/matcherrelation/singlecount")
    ApiResult<SingleCountDto> getSingleCount(@QueryParameter("userId") String userId);

    @HttpGet("/matcherrelation/matchercount")
    ApiResult<MatcherCountDto> getMatcherCount(@QueryParameter("userId") String userId);

    @HttpPost("/matcherrelation/relationship")
    ApiResult relationship(@BodyParameter("singleUserId") String singleUserId, @BodyParameter("relationship") String relationShip);

    @HttpPost("/matcherrelation/matchersaid")
    ApiResult matcherSaid(@BodyParameter("singleUserId") String singleUserId, @BodyParameter("matcherSaid") String matcherSaid);

    @HttpGet("/matcherrelation/matchersaid")
    ApiResult<MyMatchersDto> getMatcherSaid(@QueryParameter("userId") String userId);

}
