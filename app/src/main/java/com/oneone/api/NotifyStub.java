package com.oneone.api;

import com.oneone.modules.msg.dto.NotifyListDto;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.QueryParameter;

/**
 * Created by here on 18/6/14.
 */

public interface NotifyStub extends ServiceStub {
    @HttpGet("/notify/list")
    ApiResult<NotifyListDto> getNotifyList(@QueryParameter("lastNotifyTime") Long lastNotifyTime
            , @QueryParameter("pageCount") int pageCount, @QueryParameter("lastReadTime") Long lastReadTime);
}
