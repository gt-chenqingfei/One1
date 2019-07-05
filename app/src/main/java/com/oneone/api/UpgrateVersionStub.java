package com.oneone.api;

import com.oneone.modules.timeline.dto.TimeLineContainerDTO;
import com.oneone.modules.upgrate.dto.UpgrateDTO;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.HttpGet;
import com.oneone.restful.annotation.QueryParameter;

public interface UpgrateVersionStub extends ServiceStub {

    @HttpGet("/appversion/lastversion")
    ApiResult<UpgrateDTO> lastVersion(@QueryParameter("platform") String platform);

}
