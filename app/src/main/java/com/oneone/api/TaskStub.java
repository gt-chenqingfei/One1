package com.oneone.api;

import com.oneone.modules.task.dto.LoginReceiveAwardDTO;
import com.oneone.restful.ApiResult;
import com.oneone.restful.ServiceStub;
import com.oneone.restful.annotation.HttpGet;

/**
 * Created by here on 18/5/2.
 */

public interface TaskStub extends ServiceStub {
    @HttpGet("/task/login/receiveaward")
    ApiResult<LoginReceiveAwardDTO> taskLoginReceiveAward();
}
