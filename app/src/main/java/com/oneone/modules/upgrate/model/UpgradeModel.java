package com.oneone.modules.upgrate.model;

import android.content.Context;

import com.oneone.api.RestfulAPI;
import com.oneone.api.RestfulAPIFactory;
import com.oneone.api.UpgrateVersionStub;
import com.oneone.framework.ui.BaseModel;
import com.oneone.modules.upgrate.contract.UpgradeContract;
import com.oneone.modules.upgrate.dto.UpgrateDTO;
import com.oneone.restful.ApiResult;

/**
 * 版本升级业务 Model
 * <p>
 * Created by ZhaiDongyang on 2018/6/27
 */
public class UpgradeModel extends BaseModel implements UpgradeContract.Model {

    private UpgrateVersionStub upgrateVersionStub;

    public UpgradeModel(Context context) {
        super(context);
        RestfulAPIFactory restfulAPIFactory = new RestfulAPIFactory(context);
        upgrateVersionStub = restfulAPIFactory.create(UpgrateVersionStub.class, RestfulAPI.BASE_API_URL, RestfulAPI.getParams(context));
    }

    @Override
    public ApiResult<UpgrateDTO> lastversion(String platform) {
        return upgrateVersionStub.lastVersion(platform);
    }
}
