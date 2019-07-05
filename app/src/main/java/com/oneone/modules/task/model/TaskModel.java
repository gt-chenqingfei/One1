package com.oneone.modules.task.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.TaskStub;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.task.dto.LoginReceiveAwardDTO;
import com.oneone.restful.ApiResult;

/**
 * Created by here on 18/5/2.
 */

public class TaskModel extends BaseModel {
    private TaskStub taskStub;

    public TaskModel(Context context) {
        super(context);
        final RestfulAPIFactory factory = new RestfulAPIFactory(context);
        this.taskStub = factory.create(TaskStub.class,
                RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    public ApiResult<LoginReceiveAwardDTO> taskLoginReceiveAward() {
        ApiResult<LoginReceiveAwardDTO> loginReceiveAward = null;
        try {
            final ApiResult<LoginReceiveAwardDTO> result = this.taskStub.taskLoginReceiveAward();
            if (null == result) {
                return null;
            }
            loginReceiveAward = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginReceiveAward;
    }
}
