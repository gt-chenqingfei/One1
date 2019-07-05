package com.oneone.modules.matcher.relations.model;

import android.content.Context;

import com.oneone.api.MatcherRelationsStub;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.constants.ApiStatus;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.matcher.relations.bean.SingleInfo;
import com.oneone.modules.matcher.relations.dto.MatcherCountDto;
import com.oneone.modules.matcher.relations.dto.MyMatchersDto;
import com.oneone.modules.matcher.relations.dto.MySinglesDto;
import com.oneone.modules.matcher.relations.dto.SingleCountDto;
import com.oneone.restful.ApiResult;

import java.util.List;

/**
 * @author qingfei.chen
 * @since 2018/4/20.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class SinglesModel extends BaseModel {
    MatcherRelationsStub mRelationsStub = null;

    public SinglesModel(Context context) {
        super(context);
        mRelationsStub = new RestfulAPIFactory(context).create(MatcherRelationsStub.class);
    }

    public ApiResult<MySinglesDto> getMySingles(int skip, int pageCount) {
        return mRelationsStub.getMySingles(skip, pageCount);
    }

    public ApiResult<MySinglesDto> getOtherSingles(String userId, int skip, int pageCount) {
        List<SingleInfo> singleInfos = null;
        try {
            return mRelationsStub.getOthersSingles(userId, skip, pageCount);
        } catch (Exception e) {

        }
        return null;
    }

    public ApiResult<MyMatchersDto> getMyMatcher() {
        return mRelationsStub.getMyMatcher();
    }

    public ApiResult matcherSaid(String userId, String matherSaid) {
        return mRelationsStub.matcherSaid(userId, matherSaid);
    }

    public ApiResult relationShip(String userId, String relationShip) {
        return mRelationsStub.relationship(userId, relationShip);
    }

    public ApiResult<MatcherCountDto> getMatcherCount(String userId) {
        return mRelationsStub.getMatcherCount(userId);
    }

    public ApiResult<SingleCountDto> getSingleCount(String userId) {
        return mRelationsStub.getSingleCount(userId);
    }

    public ApiResult<MyMatchersDto> getMatcherSaid(String userId) {
        return mRelationsStub.getMatcherSaid(userId);
    }

}
